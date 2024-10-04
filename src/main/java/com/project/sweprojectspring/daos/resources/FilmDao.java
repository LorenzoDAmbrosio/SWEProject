package com.project.sweprojectspring.daos.resources;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.resources.Film;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FilmDao")
public class FilmDao extends DAO<Film> {
    @Override
    public Result<Film> create(Film film) {
        try {
            film.setId(0L);
            entityManager.merge(film);
            return Result.success(film);
        } catch (DataIntegrityViolationException e) {
            //return Result.fail("Film with this Filmname: "+film.getFilmname()+" already exists");
            return  Result.fail("a");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<Film>> retrieveAll() {
        try {
            List<Film> result= entityManager.createQuery(
                            "SELECT u FROM Film u WHERE true"
                            , Film.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Film> retrieveOne(Film filter) {
        try {
            Film result= entityManager.createQuery("SELECT f FROM Film f WHERE " +
                        "(f.id = :id OR :id IS NULL) AND " +
                        "(f.title = :title OR :title IS NULL) AND " +
                        "(f.author = :author OR :author IS NULL)", Film.class)
                    .setParameter("id", filter.getId())
                    .setParameter("title", filter.getTitle())
                    .setParameter("author", filter.getAuthor())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Film> update(Film film) {


        return Result.fail("Non implementato");
    }

    @Override
    public Result<Film> delete(Film film) {
        try {
            Result<Film> filmToDeleteResult = retrieveOne(film);
            if (filmToDeleteResult.isFailed()) {
                return Result.fail("Film not found");
            }
            entityManager.remove(filmToDeleteResult.toValue());
            return Result.success(filmToDeleteResult.toValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(Film film) {
        return count(film) > 0;
    }
    @Override
    public Long count(Film partialFilm) {
        Long count = entityManager.createQuery("SELECT COUNT(f) FROM Film f WHERE " +
                        "(f.id = :id OR :id IS NULL) AND " +
                        "(f.title = :title OR :title IS NULL) AND " +
                        "(f.author = :author OR :author IS NULL)", Long.class)
                .setParameter("id", partialFilm.getId())
                .setParameter("title", partialFilm.getTitle())
                .setParameter("author", partialFilm.getAuthor())
                .getSingleResult();
        return count;
    }

}
