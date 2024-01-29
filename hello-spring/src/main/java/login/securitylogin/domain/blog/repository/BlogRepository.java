package login.securitylogin.domain.blog.repository;

import login.securitylogin.domain.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}

