package com.stylefeng.guns.rest.modular.film;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.wuyan.film.FilmService;
import com.wuyan.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Autowired
    MtimeActorTMapper actorTMapper;

    @Autowired
    MtimeFilmActorTMapper filmActorTMapper;

    @Autowired
    MtimeFilmInfoTMapper filmInfoTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper hallFilmInfoTMapper;

    @Autowired
    MtimeFilmTMapper filmTMapper;

    @Autowired
    MtimeSourceDictTMapper areaMapper;


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

    /**
     * 查询影片详细信息
     * @param id
     * @return
     */
    @Override
    public FilmDetailResponseVO getFilmDetail(Integer id) {
        // zy
        EntityWrapper<MtimeFilmT> entity = new EntityWrapper<>();
        entity.eq("UUID", id);
        List<MtimeFilmT> films = mtimeFilmTMapper.selectList(entity);
        if (CollectionUtils.isEmpty(films)) {
            return null;
        }
        MtimeFilmT filmById = films.get(0);

        EntityWrapper<MtimeFilmInfoT> entity1 = new EntityWrapper<>();
        entity1.eq("film_id", id);
        List<MtimeFilmInfoT> filmInfoT = filmInfoTMapper.selectList(entity1);
        if (CollectionUtils.isEmpty(filmInfoT)) {
            return null;
        }
        MtimeFilmInfoT filmInfoById = filmInfoT.get(0);


        EntityWrapper<MtimeHallFilmInfoT> entity2 = new EntityWrapper<>();
        entity2.eq("film_id", id);
        List<MtimeHallFilmInfoT> hallFilmInfoTS = hallFilmInfoTMapper.selectList(entity2);
        if (CollectionUtils.isEmpty(hallFilmInfoTS)) {
            return null;
        }
        MtimeHallFilmInfoT hallFilmInfoById = hallFilmInfoTS.get(0);

        //获取演员信息
        String[] actors = hallFilmInfoById.getActors().split(",");
        ActorInfos actorInfos = new ActorInfos();

        ArrayList<ActorMsg> actorMsgs = new ArrayList<>();

        for (int i = 0; i < actors.length; i++) {
            String actorName = actors[i];
            ActorMsg actorMsg = new ActorMsg();
            actorMsg.setDirectorName(actorName);

            // zy
            EntityWrapper<MtimeActorT> entity3 = new EntityWrapper<>();
            entity3.eq("actor_name", actorName);
            List<MtimeActorT> mtimeActorTS = actorTMapper.selectList(entity3);
            if (CollectionUtils.isEmpty(mtimeActorTS)) {
                return null;
            }
            MtimeActorT actorByName = mtimeActorTS.get(0);

            actorMsg.setImgAddress(actorByName.getActorImg());
            int actorId = actorByName.getUuid();

            // zy
            EntityWrapper<MtimeFilmActorT> entity4 = new EntityWrapper<>();
            entity4.eq("actor_id", actorId).eq("film_id",id);
            List<MtimeFilmActorT> mtimeFilmActorTS = filmActorTMapper.selectList(entity4);
            if (CollectionUtils.isEmpty(mtimeFilmActorTS)) {
                return null;
            }
            MtimeFilmActorT filmRole = mtimeFilmActorTS.get(0);

            actorMsg.setRoleName(filmRole.getRoleName());
            actorMsgs.add(actorMsg);
        }
        //获取导演信息
        ActorMsg directorVO = new ActorMsg();
        MtimeActorT director = actorTMapper.getActorById(filmInfoById.getDirectorId());
        directorVO.setDirectorName(director.getActorName());
        directorVO.setImgAddress(director.getActorImg());
        directorVO.setRoleName("");

        //完善演员组信息
        actorInfos.setActors(actorMsgs);
        actorInfos.setDirector(directorVO);

        //获得Info4
        FilmMsg filmMsg = new FilmMsg();
        filmMsg.setActors(actorInfos);
        filmMsg.setBiopgraphy(filmInfoById.getBiography());
        filmMsg.setFilmId(id.toString());
        //获得电影图片集
        String[] movieImgs = filmInfoById.getFilmImgs().split(",");
        Map<String,String> imgMap = new HashMap<>();
        imgMap.put("mainImg",movieImgs[0]);
        for (int i = 1; i < movieImgs.length; i++) {
            imgMap.put("img0"+i,movieImgs[i]);
        }
        filmMsg.setImgVO(imgMap);


        FilmDetailResponseVO filmDetail = new FilmDetailResponseVO();
        filmDetail.setFilmEnName(filmInfoById.getFilmEnName());
        filmDetail.setFilmId(id.toString());
        filmDetail.setFilmName(filmById.getFilmName());
        filmDetail.setImgAddress(filmById.getImgAddress());
        filmDetail.setInfo01(hallFilmInfoById.getFilmCats());
        filmDetail.setInfo02((areaMapper.getAreaById(filmById.getFilmArea())).getShowName() + "/" + hallFilmInfoById.getFilmLength()+"分钟");
        filmDetail.setInfo03(filmById.getFilmTime());
        filmDetail.setInfo04(filmMsg);
        filmDetail.setScore(filmById.getFilmScore());
        filmDetail.setScoreNum(filmInfoById.getFilmScoreNum().toString());
        filmDetail.setTotalBox(filmById.getFilmBoxOffice().toString());

        return filmDetail;
    }

    /**
     * 按影片类型查找电影
     * @param
     */
    @Override
    public BaseRespVO getFilmsByShowType(FilmQueryRequestVO filmQueryRequestVO) {
        int showType = filmQueryRequestVO.getShowType();
        int nowPage = filmQueryRequestVO.getNowPage();
        int pageSize = filmQueryRequestVO.getPageSize();

        PageHelper.startPage(nowPage,pageSize);

        EntityWrapper<MtimeFilmT> entity = new EntityWrapper<>();
        entity.eq("film_status", showType);
        List<MtimeFilmT> films = mtimeFilmTMapper.selectList(entity);

        ArrayList<FilmQueryResult> filmQueryResults = new ArrayList<>();

        for (MtimeFilmT movie : films) {
            FilmQueryResult film = new FilmQueryResult();
            film.setBoxNum(movie.getFilmBoxOffice().longValue());
            film.setExpectNum(movie.getFilmPresalenum().longValue());

            EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntity = new EntityWrapper<>();
            entity.eq("UUID", movie.getUuid());
            List<MtimeHallFilmInfoT> movieInfos = hallFilmInfoTMapper.selectList(hallFilmInfoTEntity);
            MtimeHallFilmInfoT movieInfo = movieInfos.get(0);

            film.setFilmCats(movieInfo.getFilmCats());
            film.setFilmId(movie.getUuid().toString());
            film.setFilmLength(movieInfo.getFilmLength());
            film.setFilmName(movie.getFilmName());
            film.setFilmScore(movie.getFilmScore());
            film.setFilmType(showType);
            film.setImgAddress(movie.getImgAddress());
            film.setScore(movie.getFilmScore());
            film.setShowTime(movie.getFilmTime());

            filmQueryResults.add(film);
        }

        PageInfo<FilmQueryResult> filmPageInfo = new PageInfo<>(filmQueryResults);
        long total = filmPageInfo.getTotal();

        //返回前端
        BaseRespVO baseRespVO = new BaseRespVO();
        baseRespVO.setData(filmQueryResults);
        baseRespVO.setImgPre("http://img.meetingshop.cn/");
        baseRespVO.setMsg("");
        baseRespVO.setStatus(0);
        baseRespVO.setNowPage(nowPage);
        baseRespVO.setTotalPage(total);

        return baseRespVO;

    }

    /**
     * 按影片名查找电影
     * @param kw
     */
    @Override
    public FilmQueryResult getFilmsByKeyword(String kw) {
        MtimeFilmT movie = filmTMapper.getFilmByKeyword(kw);

        FilmQueryResult film = new FilmQueryResult();

        film.setBoxNum(movie.getFilmBoxOffice().longValue());
        film.setExpectNum(movie.getFilmPresalenum().longValue());

        MtimeHallFilmInfoT movieInfo = hallFilmInfoTMapper.getHallFilmInfoById(movie.getUuid());
        film.setFilmCats(movieInfo.getFilmCats());
        film.setFilmId(movie.getUuid().toString());
        film.setFilmLength(movieInfo.getFilmLength());
        film.setFilmName(movie.getFilmName());
        film.setFilmScore(movie.getFilmScore());
        film.setFilmType(movie.getFilmType());
        film.setImgAddress(movie.getImgAddress());
        film.setScore(movie.getFilmScore());
        film.setShowTime(movie.getFilmTime());

        return film;
    }
}
