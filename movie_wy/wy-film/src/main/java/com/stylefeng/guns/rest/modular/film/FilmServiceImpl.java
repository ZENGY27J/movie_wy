package com.stylefeng.guns.rest.modular.film;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.wuyan.film.FilmService;
import com.wuyan.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = FilmService.class)
@Component
public class FilmServiceImpl implements FilmService {

    @Autowired
    private MtimeFilmTMapper mtimeFilmTMapper;
    @Autowired
    private MtimeBannerTMapper mtimeBannerTMapper;
    @Autowired
    private MtimeSourceDictTMapper mtimeSourceDictTMapper;
    @Autowired
    private MtimeYearDictTMapper mtimeYearDictTMapper;
    @Autowired
    private MtimeCatDictTMapper mtimeCatDictTMapper;
    @Override
    public List<BannerVO> getBanner() {
        EntityWrapper<MtimeBannerT> wrapper = new EntityWrapper<>();
        wrapper.eq("is_valid",1);
        List<MtimeBannerT> mtimeBannerTS = mtimeBannerTMapper.selectList(wrapper);
        List<BannerVO> bannerVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(mtimeBannerTS)){
            return bannerVOS;
        }
        for (MtimeBannerT mtimeBannerT : mtimeBannerTS) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(mtimeBannerT.getUuid());
            bannerVO.setBannerAddress(mtimeBannerT.getBannerAddress());
            bannerVO.setBannerUrl(mtimeBannerT.getBannerUrl());
            bannerVOS.add(bannerVO);
        }
        return bannerVOS;
    }

    @Override
    public FilmVO getHotFilm(Integer count, Boolean isLimit) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status",1);
        List<MtimeFilmT> mtimeFilmTS ;
        Integer integer = mtimeFilmTMapper.selectCount(wrapper);
        if (isLimit){
            Page page = new Page(1,count);
            mtimeFilmTS = mtimeFilmTMapper.selectPage(page,wrapper);
        }else{
            mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
        }
        List<FilmInfoVO> hotFilm = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmInfoVO filmInfoVO = new FilmInfoVO();
            filmInfoVO.setFilmId(mtimeFilmT.getUuid()+"");
            filmInfoVO.setFilmType(mtimeFilmT.getFilmType());
            filmInfoVO.setImgAddress(mtimeFilmT.getImgAddress());
            filmInfoVO.setFilmName(mtimeFilmT.getFilmName());
            filmInfoVO.setFilmScore(mtimeFilmT.getFilmScore());
            hotFilm.add(filmInfoVO);
        }
        FilmVO filmVO = new FilmVO();
        filmVO.setFilmNum(integer);
        filmVO.setFilmInfo(hotFilm);
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilm(Integer count, Boolean isLimit) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status",2);
        List<MtimeFilmT> mtimeFilmTS ;
        Integer integer = mtimeFilmTMapper.selectCount(wrapper);
        if (isLimit){
            Page page = new Page(1,count);
            mtimeFilmTS = mtimeFilmTMapper.selectPage(page,wrapper);
        }else{
            mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
        }
        List<FilmInfoVO> soonFilm = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmInfoVO filmInfoVO = new FilmInfoVO();
            filmInfoVO.setFilmId(mtimeFilmT.getUuid()+"");
            filmInfoVO.setFilmType(mtimeFilmT.getFilmType());
            filmInfoVO.setImgAddress(mtimeFilmT.getImgAddress());
            filmInfoVO.setFilmName(mtimeFilmT.getFilmName());
            filmInfoVO.setExpectNum(mtimeFilmT.getFilmPresalenum());
            filmInfoVO.setShowTime(mtimeFilmT.getFilmTime());
            soonFilm.add(filmInfoVO);
        }
        FilmVO filmVO = new FilmVO();
        filmVO.setFilmNum(integer);
        filmVO.setFilmInfo(soonFilm);
        return filmVO;
    }

    @Override
    public List<FilmRankingVO> getBoxRanking(Integer count) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_box_office",false);
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
        List<FilmRankingVO> list = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmRankingVO filmRankingVO = new FilmRankingVO();
            filmRankingVO.setFilmId(mtimeFilmT.getUuid());
            filmRankingVO.setImgAddress(mtimeFilmT.getImgAddress());
            filmRankingVO.setFilmName(mtimeFilmT.getFilmName());
            filmRankingVO.setBoxNum(mtimeFilmT.getFilmBoxOffice());
            list.add(filmRankingVO);
        }
        return list;
    }

    @Override
    public List<FilmRankingVO> getExpectRanking(Integer count) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_preSaleNum",false);
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
        List<FilmRankingVO> list = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmRankingVO filmRankingVO = new FilmRankingVO();
            filmRankingVO.setFilmId(mtimeFilmT.getUuid());
            filmRankingVO.setImgAddress(mtimeFilmT.getImgAddress());
            filmRankingVO.setFilmName(mtimeFilmT.getFilmName());
            filmRankingVO.setExpectNum(mtimeFilmT.getFilmPresalenum());
            list.add(filmRankingVO);
        }
        return list;
    }

    @Override
    public List<FilmRankingVO> getTop100(Integer count) {
        EntityWrapper<MtimeFilmT> wrapper = new EntityWrapper<>();
        wrapper.orderBy("film_score",false);
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);
        List<FilmRankingVO> list = new ArrayList<>();
        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            FilmRankingVO filmRankingVO = new FilmRankingVO();
            filmRankingVO.setFilmId(mtimeFilmT.getUuid());
            filmRankingVO.setImgAddress(mtimeFilmT.getImgAddress());
            filmRankingVO.setFilmName(mtimeFilmT.getFilmName());
            filmRankingVO.setScore(mtimeFilmT.getFilmScore());
            list.add(filmRankingVO);
        }
        return list;
    }

    @Override
    public List<CatVO> getCat(Integer catId) {
        List<MtimeCatDictT> mtimeCatDictTS = mtimeCatDictTMapper.selectList(new EntityWrapper<>());
        List<CatVO> list = new ArrayList<>();
        for (MtimeCatDictT catDictT : mtimeCatDictTS) {
            CatVO catVO = new CatVO();
            catVO.setCatId(catDictT.getUuid());
            catVO.setCatName(catDictT.getShowName());
            int cat = 0;
            if (catDictT.getUuid() != null){
                cat = catDictT.getUuid();
            }
            if (cat == catId){
                catVO.setActive(true);
            }else{
                catVO.setActive(false);
            }
            list.add(catVO);
        }
        return list;
    }

    @Override
    public List<SourceVO> getSource(Integer sourceId) {
        List<MtimeSourceDictT> mtimeSourceDictTS = mtimeSourceDictTMapper.selectList(new EntityWrapper<>());
        List<SourceVO> list = new ArrayList<>();
        for (MtimeSourceDictT sourceDictT : mtimeSourceDictTS) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(sourceDictT.getUuid());
            sourceVO.setSourceName(sourceDictT.getShowName());
            int id = 0;
            if (sourceDictT.getUuid() != null){
                id = sourceDictT.getUuid();
            }
            if (id == sourceId){
                sourceVO.setActive(true);
            }else{
                sourceVO.setActive(false);
            }
            list.add(sourceVO);
        }
        return list;
    }

    @Override
    public List<YearVO> getYear(Integer yearId) {
        List<MtimeYearDictT> mtimeYearDictTS = mtimeYearDictTMapper.selectList(new EntityWrapper<>());
        List<YearVO> list = new ArrayList<>();
        for (MtimeYearDictT yearDictT : mtimeYearDictTS) {
            YearVO yearVO = new YearVO();
            yearVO.setYearId(yearDictT.getUuid());
            yearVO.setYearName(yearDictT.getShowName());
            int id = 0;
            if (yearDictT.getUuid() != null){
                id = yearDictT.getUuid();
            }
            if (id == yearId){
                yearVO.setActive(true);
            }else{
                yearVO.setActive(false);
            }
            list.add(yearVO);
        }
        return list;
    }
}