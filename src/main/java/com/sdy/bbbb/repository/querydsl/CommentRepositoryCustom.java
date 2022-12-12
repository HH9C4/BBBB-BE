package com.sdy.bbbb.repository.querydsl;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> searchCommentsInMyPosts(Account account);
}
