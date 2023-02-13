package org.Simbot.db;

import love.forte.simbot.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.db
 * @date 2022/12/7 13:22
 */
@Component
public class dbUtils {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long Id(ID id) {
        return parseLong(String.valueOf(id).trim());
    }

    /**
     * 判断重复抽签
     *
     * @param id
     */
    public void setLoadDb(ID id) {
        String sql = "INSERT INTO loadday VALUES(" + Id(id) + ')';
        jdbcTemplate.execute(sql);
    }

    /**
     * 清空抽签数据
     */
    public void clearLoad() {
        String sql = "TRUNCATE loadday;";
        jdbcTemplate.execute(sql);
    }

    /**
     * 注册签到表
     */
    public void setEnroll(ID id, String Time) {
        String sql = "INSERT INTO sumplus(id,sum,daytime,sumday) VALUES(" + id + ",50,\"" + Time + "\",1);";
        jdbcTemplate.execute(sql);
    }

    /**
     * 每日签到
     *
     * @param id
     * @param Time
     */
    public void setSign(ID id, String Time) {
        String time = "'" + Time + "'";
        String IdStr = "'" + id + "'";

        String sql = MessageFormat.format("""
                        UPDATE sumplus
                        SET sum = sum + 100, sumday = sumday + 1, daytime=	{0}	WHERE id ={1}	AND daytime <>	{2}	;""",
                time,
                IdStr,
                time);
        jdbcTemplate.execute(sql);
    }

    /**
     * 查询签到数据
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSelect(ID id) {
        String IdStr = "'" + id + "'";
        String sql = MessageFormat.format("SELECT sum,sumday FROM sumplus WHERE id={0};", IdStr);
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 添加群黑名单
     *
     * @param groupId
     */
    public void blackList(ID groupId) {
        String sql = "INSERT INTO blacklist VALUES(" + Id(groupId) + ");";
        jdbcTemplate.execute(sql);
    }

    /**
     * @param id
     * @return
     */

    public List<Map<String, Object>> selectBlackList(ID id) {
        String sql = "SELECT id FROM blacklist WHERE id=" + Id(id) + ';';
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 删除黑名单
     *
     * @param id
     */
    public void delBlackList(ID id) {
        String sql = "DELETE FROM blacklist WHERE id=" + Id(id) + ';';
        jdbcTemplate.execute(sql);
    }
}
