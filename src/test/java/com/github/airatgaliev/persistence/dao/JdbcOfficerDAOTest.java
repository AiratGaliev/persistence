package com.github.airatgaliev.persistence.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.airatgaliev.persistence.entities.Officer;
import com.github.airatgaliev.persistence.entities.Rank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class JdbcOfficerDAOTest {

  @Autowired
  private OfficerDAO officerDAO;

  @Test
  public void save() {
    Officer officer = new Officer(Rank.ENSIGN, "Airat", "Galiev");
    officer = officerDAO.save(officer);
    assertNotNull(officer.getId());
  }

  @Test
  public void findByIdThatExists() {
    Optional<Officer> officer = officerDAO.findById(1);
    assertTrue(officer.isPresent());
    assertEquals(1, officer.get().getId().intValue());
  }

  @Test
  public void findByIdThatDoesNotExist() {
    Optional<Officer> officer = officerDAO.findById(999);
    assertFalse(officer.isPresent());
  }

  @Test
  public void count() {
    assertEquals(5, officerDAO.count());
  }

  @Test
  public void findAll() {
    List<String> dbNames = officerDAO.findAll().stream()
        .map(Officer::getLast)
        .collect(Collectors.toList());
    assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
  }

  @Test
  public void delete() {
    IntStream.rangeClosed(1, 5)
        .forEach(id -> {
          Optional<Officer> officers = officerDAO.findById(id);
          assertTrue(officers.isPresent());
          officerDAO.delete(officers.get());
        });
    assertEquals(0, officerDAO.count());
  }

  @Test
  public void existsById() {
    IntStream.rangeClosed(1, 5)
        .forEach(id -> assertTrue(officerDAO.existsById(id)));
  }
}