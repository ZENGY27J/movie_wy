package com.stylefeng.guns.rest.common.persistence.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallFilmInfoT;
import com.wuyan.cinema.CinemaService;
import com.wuyan.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {
    @Autowired
    MtimeAreaDictTMapper mtimeAreaDictTMapper;
    @Autowired
    MtimeBrandDictTMapper mtimeBrandDictTMapper;
    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;
    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;
    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;
    @Autowired
    MtimeFilmTMapper mtimeFilmTMapper;


    @Override
    public CinemaGetFieldsVo getFields(Integer cinemaId) {
        CinemaGetFieldsVo cinemaGetFieldsVo = new CinemaGetFieldsVo();

        CinemaGetFieldsDataVo cinemaFieldsData = new CinemaGetFieldsDataVo();
        List<FilmInfoVo> filmList = new ArrayList<>();
        //获得影院信息并将其封装到对应的vo 对象中
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);
        inputCinemaInfo(cinemaFieldsData, mtimeCinemaT);

        //得到该影院的放映场次信息表并且封装到对象中
        List<MtimeFieldT> mtimeFieldTs = mtimeFieldTMapper.selectByCinemaId(cinemaId);
        setFilmInfoVo(cinemaFieldsData, mtimeFieldTs, filmList);

        cinemaGetFieldsVo.setData(cinemaFieldsData);
        cinemaGetFieldsVo.setImgPre(mtimeCinemaT.getImgAddress());
        cinemaGetFieldsVo.setStatus(0);
        return cinemaGetFieldsVo;
    }

    @Override
    public CinemaFieldInfo getFieldInfo(Integer cinemaId, Integer fieldId) {
        CinemaFieldInfo cinemaFieldInfo = new CinemaFieldInfo();
        CinemaFieldInfoDataVo cinemaFieldInfoDataVo = new CinemaFieldInfoDataVo();
        //获得影院信息并将其封装到对应的vo 对象中
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);
        CinemaInfoVo cinemaInfo = new CinemaInfoVo();
        inputCinemaInfo(cinemaInfo, mtimeCinemaT);

        //获得电影信息并封装到对应的vo 对象中
        FilmInfoVo filmInfo = getFilmInfo(cinemaId, fieldId);

        //获得影厅信息并封装到对应的Vo 对象中
        HallInfoVo hallInfoVo = getHallInfo(cinemaId, fieldId);

        cinemaFieldInfoDataVo.setCinemaInfo(cinemaInfo);
        cinemaFieldInfoDataVo.setFilmInfo(filmInfo);
        cinemaFieldInfoDataVo.setHallInfo(hallInfoVo);
        cinemaFieldInfo.setStatus(0);
        cinemaFieldInfo.setData(cinemaFieldInfoDataVo);

        return cinemaFieldInfo;
    }

    private HallInfoVo getHallInfo(Integer cinemaId, Integer fieldId) {
        HallInfoVo hallInfoVo = new HallInfoVo();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        hallInfoVo.setHallFieldId(mtimeFieldT.getHallId());
        hallInfoVo.setHallName(mtimeFieldT.getHallName());
        hallInfoVo.setPrice(mtimeFieldT.getPrice());

//        hallInfoVo.setSeatFile();
//        hallInfoVo.setSoldSeats();

        return hallInfoVo;
    }

    private FilmInfoVo getFilmInfo(Integer cinemaId, Integer fieldId) {
        FilmInfoVo filmInfoVo = new FilmInfoVo();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        Integer filmId = mtimeFieldT.getFilmId();
        //得到影厅信息
        EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
        hallFilmInfoTEntityWrapper.eq("film_id", filmId);
        List<MtimeHallFilmInfoT> mtimeHallFilmInfoTS = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
        MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTS.get(0);

        filmInfoVo.setActors(mtimeHallFilmInfoT.getActors());
        filmInfoVo.setFilmCats(mtimeHallFilmInfoT.getFilmCats());
        filmInfoVo.setFilmId(filmId);
        filmInfoVo.setFilmName(mtimeHallFilmInfoT.getFilmName());
        filmInfoVo.setImgAddress(mtimeHallFilmInfoT.getImgAddress());
        filmInfoVo.setFilmLength(mtimeHallFilmInfoT.getFilmLength());
        return filmInfoVo;
    }

    private void setFilmInfoVo(CinemaGetFieldsDataVo cinemaFieldsData, List<MtimeFieldT> mtimeFieldTs, List<FilmInfoVo> filmList) {
        List<Integer> filmIds = new ArrayList<>();

        //将所有要放映的电影id 存在一个列表中
        for (MtimeFieldT mtimeFieldT : mtimeFieldTs) {
            if (filmIds != null) {
                if (!filmIds.contains(mtimeFieldT.getFilmId())) {
                    filmIds.add(mtimeFieldT.getFilmId());
                }
            }
        }

        for (Integer filmId : filmIds) {
            FilmInfoVo filmInfoVo = new FilmInfoVo();
            ArrayList<FilmFieldVo> filmFieldVos = new ArrayList<>();

            EntityWrapper<MtimeHallFilmInfoT> hallFilmInfoTEntityWrapper = new EntityWrapper<>();
            hallFilmInfoTEntityWrapper.eq("film_id", filmId);
            List<MtimeHallFilmInfoT> mtimeHallFilmInfoTS = mtimeHallFilmInfoTMapper.selectList(hallFilmInfoTEntityWrapper);
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTS.get(0);
            //给filmInfoVo 除了 filmfieldVo 之外的属性设置属性值
            setFilmInfoVoElse(filmId, filmInfoVo, mtimeHallFilmInfoT);

            for (MtimeFieldT mtimeFieldT : mtimeFieldTs) {
                if (mtimeFieldT.getFilmId() == filmId) {
                    FilmFieldVo filmFieldVo = new FilmFieldVo();
                    filmFieldVo.setFieldId(mtimeFieldT.getUuid());
                    filmFieldVo.setBeginTime(mtimeFieldT.getBeginTime());
                    filmFieldVo.setEndTime(mtimeFieldT.getEndTime());
                    filmFieldVo.setHallName(mtimeFieldT.getHallName());
                    filmFieldVo.setPrice(mtimeFieldT.getPrice());
                    filmFieldVo.setLanguage(mtimeHallFilmInfoT.getFilmLanguage());
                    filmFieldVos.add(filmFieldVo);
                }
                filmInfoVo.setFilmFields(filmFieldVos);
            }
            filmList.add(filmInfoVo);
        }
        cinemaFieldsData.setFilmList(filmList);
    }

    private void setFilmInfoVoElse(Integer filmId, FilmInfoVo filmInfoVo, MtimeHallFilmInfoT mtimeHallFilmInfoT) {
        filmInfoVo.setFilmId(filmId);
        filmInfoVo.setActors(mtimeHallFilmInfoT.getActors());
        filmInfoVo.setFilmName(mtimeHallFilmInfoT.getFilmName());
        filmInfoVo.setFilmType(mtimeHallFilmInfoT.getFilmLanguage());//type 没处查
        filmInfoVo.setFilmCats(mtimeHallFilmInfoT.getFilmCats());
        filmInfoVo.setImgAddress(mtimeHallFilmInfoT.getImgAddress());
    }

    private void inputCinemaInfo(CinemaGetFieldsDataVo  cinemaFieldsData, MtimeCinemaT mtimeCinemaT) {
        CinemaInfoVo cinemaInfoVo = new CinemaInfoVo();
        cinemaInfoVo.setCinemaId( mtimeCinemaT.getUuid());
        cinemaInfoVo.setImgUrl( mtimeCinemaT.getImgAddress());
        cinemaInfoVo.setCinemaName( mtimeCinemaT.getCinemaName());
        cinemaInfoVo.setCinemaAddress( mtimeCinemaT.getCinemaAddress());
        cinemaInfoVo.setCinemaPhone( mtimeCinemaT.getCinemaPhone());

        cinemaFieldsData.setCinemaInfo(cinemaInfoVo);
    }

    private void inputCinemaInfo(CinemaInfoVo  cinemaInfoVo, MtimeCinemaT mtimeCinemaT) {
        cinemaInfoVo.setCinemaId( mtimeCinemaT.getUuid());
        cinemaInfoVo.setImgUrl( mtimeCinemaT.getImgAddress());
        cinemaInfoVo.setCinemaName( mtimeCinemaT.getCinemaName());
        cinemaInfoVo.setCinemaAddress( mtimeCinemaT.getCinemaAddress());
        cinemaInfoVo.setCinemaPhone( mtimeCinemaT.getCinemaPhone());
    }
}
