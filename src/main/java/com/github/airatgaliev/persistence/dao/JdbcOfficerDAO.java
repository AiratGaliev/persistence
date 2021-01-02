package com.github.airatgaliev.persistence.dao;

import com.github.airatgaliev.persistence.entities.Officer;
import com.github.airatgaliev.persistence.entities.Rank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspetion", "ConstantConditions"})
@Repository
public class JdbcOfficerDAO implements OfficerDAO {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  public JdbcOfficerDAO(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("officers")
        .usingGeneratedKeyColumns("id");
  }

  @Override
  public Officer save(Officer officer) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("rank", officer.getRank());
    parameters.put("first_name", officer.getFirst());
    parameters.put("last_name", officer.getLast());
    Integer newId = (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
    officer.setId(newId);
    return officer;
  }

  @Override
  public Optional<Officer> findById(Integer id) {
    if (!existsById(id)) {
      return Optional.empty();
    }
    return Optional.ofNullable(jdbcTemplate.queryForObject("select * from officers where id=?",
        (rs, rowNum) -> new Officer(rs.getInt("id"), Rank.valueOf(rs.getString("rank")),
            rs.getString("first_name"), rs.getString("last_name")), id));
  }

  @Override
  public List<Officer> findAll() {
    return jdbcTemplate.query("select * from officers",
        (rs, rowNum) -> new Officer(rs.getInt("id"), Rank.valueOf(rs.getString("rank")),
            rs.getString("first_name"), rs.getString("last_name")));
  }

  @Override
  public long count() {
    return jdbcTemplate.queryForObject("select count (*) from officers", Long.class);
  }

  @Override
  public void delete(Officer officer) {
    jdbcTemplate.update("delete from officers where id=?", officer.getId());
  }

  @Override
  public boolean existsById(Integer id) {
    return jdbcTemplate
        .queryForObject("select exists(select 1 from officers where id = ?)", Boolean.class, id);
  }
}
