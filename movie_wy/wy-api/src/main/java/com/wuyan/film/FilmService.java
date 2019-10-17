package com.wuyan.film;

import com.wuyan.film.vo.*;

import java.util.List;

public interface FilmService {

    public List<BannerVO> getBanner();

    public FilmVO getHotFilm(Integer count, Boolean isLimit);

    public FilmVO getSoonFilm(Integer count, Boolean isLimit);

    public List<FilmRankingVO> getBoxRanking(Integer count);

    public List<FilmRankingVO> getExpectRanking(Integer count);

    public List<FilmRankingVO> getTop100(Integer count);

    List<CatVO> getCat(Integer catId);

    List<SourceVO> getSource(Integer sourceId);

    List<YearVO> getYear(Integer yearId);

    FilmDetailResponseVO getFilmDetail(Integer id);

    BaseRespVO getFilmsByShowType(FilmQueryRequestVO filmQueryRequestVO);

    FilmQueryResult getFilmsByKeyword(String kw);
}
