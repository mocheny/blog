package com.example.service.impl;

import com.example.entity.Blog;
import com.example.mapper.BlogMapper;
import com.example.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 深林中的书海
 * @since 2022-02-28
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
