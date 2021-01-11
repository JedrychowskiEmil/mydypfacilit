package pl.jedrychowski.mydypfacilit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jedrychowski.mydypfacilit.DAO.DAOHibernate;
import pl.jedrychowski.mydypfacilit.Entity.News;

import java.time.LocalDate;

@Service
public class NewsService {
    private DAOHibernate daoHibernate;

    @Autowired
    public NewsService(DAOHibernate daoHibernate) {
        this.daoHibernate = daoHibernate;
    }

    public void saveOrUpdateNews(News news){
        if(news.getId() == 0){
            news.setDate(LocalDate.now());
            daoHibernate.saveOrUpdateNews(news);
        }else{
            News oldNews = daoHibernate.getNewsById(news.getId());

            oldNews.setTitle(news.getTitle());
            oldNews.setContent(news.getContent());
            oldNews.setImportant(news.isImportant());

            daoHibernate.saveOrUpdateNews(oldNews);
        }
    }

    public void deleteNews(Long id){
        daoHibernate.deleteNewsById(id);
    }
}
