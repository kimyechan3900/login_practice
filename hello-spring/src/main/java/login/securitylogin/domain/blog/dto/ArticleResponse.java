package login.securitylogin.domain.blog.dto;

import lombok.Getter;
import login.securitylogin.domain.blog.entity.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
