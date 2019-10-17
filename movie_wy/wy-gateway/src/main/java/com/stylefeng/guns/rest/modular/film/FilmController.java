package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.vo.*;
import com.wuyan.film.FilmService;
import com.wuyan.film.service.FilmModuleService;
import com.wuyan.film.vo.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("film")
public class FilmController {
    @Reference(interfaceClass = FilmService.class,check = false)
    private FilmService filmService;

    @RequestMapping("getIndex")
    public ReBaseVo filmIndex(){
        FilmIndexVO filmIndex = new FilmIndexVO();
        try {
            filmIndex.setBanners(filmService.getBanner());
            filmIndex.setHotFilms(filmService.getHotFilm(0, false));
            filmIndex.setSoonFilms(filmService.getSoonFilm(0, false));
            filmIndex.setBoxRanking(filmService.getBoxRanking(0));
            filmIndex.setExpectRanking(filmService.getExpectRanking(0));
            filmIndex.setTop100(filmService.getTop100(0));
        }catch (Throwable e){
            return ReBaseMsgVo.error(999,"系统出现异常，请联系管理员");
        }
        if (filmIndex.getBanners() == null){
            return ReBaseMsgVo.error(1,"查询失败，无banner可加载");
        }
        return ResultVO.ok("http://img.meetingshop.cn/",filmIndex);
    }

    @RequestMapping("getConditionList")
    public ReBaseVo getConditionList(FilmConditionVO filmConditionVO){
        ConditionVO conditionVO = new ConditionVO();
        try {
            conditionVO.setCatInfo(filmService.getCat(filmConditionVO.getCatId()));
            conditionVO.setSourceInfo(filmService.getSource(filmConditionVO.getSourceId()));
            conditionVO.setYearInfo(filmService.getYear(filmConditionVO.getYearId()));
        }catch (Throwable e){
            return ReBaseMsgVo.error(999,"系统出现异常，请联系管理员");
        }
        return ReBaseDataVo.ok(conditionVO);
    }


    @RequestMapping("/films/{id}")
    public BaseRespVO getFilmsDetail(@PathVariable(value = "id") Integer id){
        FilmDetailResponseVO filmDetail = filmService.getFilmDetail(id);
        if (filmDetail == null) {
            BaseRespVO baseRespVO = BaseRespVO.serviceErr();
            return baseRespVO;
        }
        return BaseRespVO.ok(filmDetail);
    }

    @RequestMapping("/getFilms")
    public BaseRespVO getRelevantFilm(FilmQueryRequestVO filmQueryRequestVO){

        if(filmQueryRequestVO.getShowType() != null){
            BaseRespVO baseRespVO = filmService.getFilmsByShowType(filmQueryRequestVO);
            return baseRespVO;
        }
        if (filmQueryRequestVO.getKw() != null){
            FilmQueryResult filmsByKeyword = filmService.getFilmsByKeyword(filmQueryRequestVO.getKw());
            return BaseRespVO.ok(filmsByKeyword);
        }
        return BaseRespVO.serviceErr();
    }
}
