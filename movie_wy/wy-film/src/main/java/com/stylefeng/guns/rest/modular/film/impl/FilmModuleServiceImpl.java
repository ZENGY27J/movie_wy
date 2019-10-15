package com.stylefeng.guns.rest.modular.film.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.wuyan.film.service.FilmModuleService;
import com.wuyan.film.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = FilmModuleService.class)
@Component
public class FilmModuleServiceImpl implements FilmModuleService {

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
    public List<ActorMsg> getAllActors(Integer id) {
        return null;
    }

    @Override
    public ActorMsg getDirector(Integer id) {
        return null;
    }

    /**
     * 查询影片详细信息
     * @param id
     * @return
     */
    @Override
    public FilmDetailResponseVO getFilmDetail(Integer id) {
        MtimeFilmT filmById = filmTMapper.getFilmById(id);

        MtimeFilmInfoT filmInfoById = filmInfoTMapper.getFilmInfoById(id);
        MtimeHallFilmInfoT hallFilmInfoById = hallFilmInfoTMapper.getHallFilmInfoById(id);

        //获取演员信息
        String[] actors = hallFilmInfoById.getActors().split(",");
        ActorInfos actorInfos = new ActorInfos();

        ArrayList<ActorMsg> actorMsgs = new ArrayList<>();

        for (int i = 0; i < actors.length; i++) {
            String actorName = actors[i];
            ActorMsg actorMsg = new ActorMsg();
            actorMsg.setDirectorName(actorName);
            MtimeActorT actorByName = actorTMapper.getActorByName(actorName);
            actorMsg.setImgAddress(actorByName.getActorImg());
            int actorId = actorByName.getUuid();
            MtimeFilmActorT filmRole = filmActorTMapper.getActorByActorId(actorId,id);
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

        List<MtimeFilmT> films = filmTMapper.getFilmByShowType(showType);

        ArrayList<FilmQueryResult> filmQueryResults = new ArrayList<>();

        for (int i = 0; i < films.size(); i++) {
            FilmQueryResult film = new FilmQueryResult();
            MtimeFilmT movie = films.get(i);
            film.setBoxNum(movie.getFilmBoxOffice().longValue());
            film.setExpectNum(movie.getFilmPresalenum().longValue());

            MtimeHallFilmInfoT movieInfo = hallFilmInfoTMapper.getHallFilmInfoById(movie.getUuid());
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
