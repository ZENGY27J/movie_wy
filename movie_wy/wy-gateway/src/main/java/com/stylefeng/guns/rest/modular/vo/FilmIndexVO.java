package com.stylefeng.guns.rest.modular.vo;

import com.wuyan.film.vo.BannerVO;
import com.wuyan.film.vo.FilmInfoVO;
import com.wuyan.film.vo.FilmRankingVO;
import com.wuyan.film.vo.FilmVO;

import java.util.List;

public class FilmIndexVO {
    private List<BannerVO> banners;
    private FilmVO hotFilms;
    private FilmVO soonFilms;
    private List<FilmRankingVO> boxRanking;
    private List<FilmRankingVO> expectRanking;
    private List<FilmRankingVO> top100;

    public List<BannerVO> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerVO> banners) {
        this.banners = banners;
    }

    public FilmVO getHotFilms() {
        return hotFilms;
    }

    public void setHotFilms(FilmVO hotFilms) {
        this.hotFilms = hotFilms;
    }

    public FilmVO getSoonFilms() {
        return soonFilms;
    }

    public void setSoonFilms(FilmVO soonFilms) {
        this.soonFilms = soonFilms;
    }

    public List<FilmRankingVO> getBoxRanking() {
        return boxRanking;
    }

    public void setBoxRanking(List<FilmRankingVO> boxRanking) {
        this.boxRanking = boxRanking;
    }

    public List<FilmRankingVO> getExpectRanking() {
        return expectRanking;
    }

    public void setExpectRanking(List<FilmRankingVO> expectRanking) {
        this.expectRanking = expectRanking;
    }

    public List<FilmRankingVO> getTop100() {
        return top100;
    }

    public void setTop100(List<FilmRankingVO> top100) {
        this.top100 = top100;
    }
}
