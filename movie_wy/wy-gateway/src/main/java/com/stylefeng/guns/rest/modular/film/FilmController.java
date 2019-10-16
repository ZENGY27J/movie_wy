package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.vo.ConditionVO;
import com.stylefeng.guns.rest.modular.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ResultVO;
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

    @Reference(interfaceClass = FilmModuleService.class,check = false)
    FilmModuleService filmModuleService;

    @RequestMapping("getIndex")
    public ResultVO filmIndex(){
        FilmIndexVO filmIndex = new FilmIndexVO();
        filmIndex.setBanners(filmService.getBanner());
        filmIndex.setHotFilms(filmService.getHotFilm(0,false));
        filmIndex.setSoonFilms(filmService.getSoonFilm(0,false));
        filmIndex.setBoxRanking(filmService.getBoxRanking(0));
        filmIndex.setExpectRanking(filmService.getExpectRanking(0));
        filmIndex.setTop100(filmService.getTop100(0));
        return ResultVO.ok(filmIndex);
    }

    @RequestMapping("getConditionList")
    public ReBaseDataVo getConditionList(FilmConditionVO filmConditionVO){
        ConditionVO conditionVO = new ConditionVO();
        conditionVO.setCatInfo(filmService.getCat(filmConditionVO.getCatId()));
        conditionVO.setSourceInfo(filmService.getSource(filmConditionVO.getSourceId()));
        conditionVO.setYearInfo(filmService.getYear(filmConditionVO.getYearId()));
        return ReBaseDataVo.ok(conditionVO);
    }


    @RequestMapping("/films/{id}")
    public BaseRespVO getFilmsDetail(@PathVariable(value = "id") Integer id){
        FilmDetailResponseVO filmDetail = filmModuleService.getFilmDetail(id);
        return BaseRespVO.ok(filmDetail);
    }

    @RequestMapping("/getFilms")
    public BaseRespVO getRelevantFilm(FilmQueryRequestVO filmQueryRequestVO){

        if(filmQueryRequestVO.getShowType() != null){
            BaseRespVO baseRespVO = filmModuleService.getFilmsByShowType(filmQueryRequestVO);
            return baseRespVO;
        }
        if (filmQueryRequestVO.getKw() != null){
            FilmQueryResult filmsByKeyword = filmModuleService.getFilmsByKeyword(filmQueryRequestVO.getKw());
            return BaseRespVO.ok(filmsByKeyword);
        }
        return BaseRespVO.serviceErr();
    }
}
