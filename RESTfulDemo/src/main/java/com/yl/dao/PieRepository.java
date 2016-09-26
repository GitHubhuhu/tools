package com.yl.dao;

import com.yl.entity.Pie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * <pre>
 *   数据库操作
 *
 * </pre>
 * <p/>
 * Created by luxiaohu at 16/9/23 15:39
 */
public interface PieRepository extends CrudRepository<Pie, Long> {
    List<Pie> findByName(String name);
}

