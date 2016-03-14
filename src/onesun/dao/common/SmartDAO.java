package onesun.dao.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class SmartDAO {
	protected static final Log log = LogFactory.getLog(SmartDAO.class);

	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/** 算整个表的条数 */
	public int getCount(String sql) throws Exception {
		log.info("[sql : ] = [" + sql + "] @SmartDAO.getCount");
		return jdbcTemplate.queryForInt(sql);
	}

	/** 带参数的统计条数 */
	public int getCount(String sql, Object[] args) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (null != args && args.length > 0) {
			sb.append(args[0]);
			int len = args.length;
			for (int i = 1; i < len; i++) {
				if (null != args[i]) {
					sb.append(",").append(args[i].toString());
				}
			}
			log.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString()
					+ "] @SmartDAO.getCount");
			return jdbcTemplate.queryForInt(sql, args);
		} else {
			return getCount(sql);
		}
	}

	public int update(String user, String sql, Object[] args) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (null != args && args.length > 0) {
			sb.append(args[0]);
			int len = args.length;
			for (int i = 1; i < len; i++) {
				if (null != args[i]) {
					sb.append(", ").append(args[i].toString());
				}
			}
			log.info(user + " execute sql=[" + sql + "]\nargs=["
					+ sb.toString() + "]");
			return jdbcTemplate.update(sql, args);
		} else {
			return update(user, sql);
		}
	}

	/** 更新 */
	public int update(String user, String sql) throws Exception {
		log.debug(user + " execute sql : " + sql);
		return jdbcTemplate.update(sql);
	}

	/** 不过是包了一下jdbc自带的那套东西，加个日志而已 */
	public int[] batchUpdate(String user, String sql, List<Object[]> batchArgs)
			throws Exception {
		log.debug(user + " execute sql=[" + sql + "]");
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	public <T> List<T> getList(Class<T> clazz, String sql) {
		log.debug("[sql]=[" + sql + "]");
		return jdbcTemplate.query(sql,
				ParameterizedBeanPropertyRowMapper.newInstance(clazz));
	}

	public <T> List<T> getList(Class<T> clazz, String sql,
			Map<String, String> args) {
		log.debug("[sql]=[" + sql + "]");
		return jdbcTemplate.query(sql,
				ParameterizedBeanPropertyRowMapper.newInstance(clazz), args);
	}

	public <T> List<T> getList(Class<T> clazz, String sql, Object[] args) {
		StringBuffer sb = new StringBuffer();
		if (null != args && args.length > 0) {
			sb.append(args[0]);
			for (int i = 0; i < args.length; i++) {
				if (null != args[i]) {
					sb.append(",").append(args[i].toString());
				}
			}
		}
		log.debug("[sql]=[" + sql + "]\nargs=[" + sb.toString() + "]");
		return jdbcTemplate.query(sql,
				ParameterizedBeanPropertyRowMapper.newInstance(clazz), args);
	}

	public <T> T find(Class<T> clazz, String sql) throws Exception {
		try {
			log.debug("[sql]=[" + sql + "]");
			return jdbcTemplate.queryForObject(sql,
					ParameterizedBeanPropertyRowMapper.newInstance(clazz));
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public <T> T find(Class<T> clazz, String sql, Map<String, String> args)
			throws Exception {
		try {
			log.debug("[sql]=[" + sql + "]");
			return jdbcTemplate.queryForObject(sql,
					ParameterizedBeanPropertyRowMapper.newInstance(clazz));
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public <T> T find(Class<T> clazz, String sql, Object[] args) throws Exception {
		try {
			StringBuffer sb = new StringBuffer();
			if (null != args && args.length > 0) {
				sb.append(args[0]);
				for (int i = 1; i < args.length; i++) {
					if (null != args[i]) {
						sb.append(",").append(args[i].toString());
					}
				}
			}
	
			log.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
			return jdbcTemplate.queryForObject(sql,
					ParameterizedBeanPropertyRowMapper.newInstance(clazz));
		} catch (EmptyResultDataAccessException e) {
			// TIPS: 这里都这么写是因为这个异常可以忽略不计
			return null;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> getFields(Class<T> clazz, String sql, Object[] args, String columnName) throws Exception {
		try {
		StringBuffer sb = new StringBuffer();
		if (null != args && args.length > 0) {
			sb.append(args[0]);
			for (int i = 1; i < args.length; i++) {
				if (null != args[i]) {
					sb.append(",").append(args[i].toString());
				}
			}
		}
		
		log.debug("[sql]=[" + sql + "]\n[args]=[" + sb.toString() + "]");
		final String name = columnName;
		return jdbcTemplate.query(sql, new ParameterizedRowMapper() {
			public T mapRow(ResultSet rs, int i) throws SQLException {
				return (T) rs.getObject(name);
			}
		}, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			throw e;
		}
	}
}
