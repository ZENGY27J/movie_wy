package com.stylefeng.guns.rest.common.persistence.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.wuyan.cinema.CinemaService;
import com.wuyan.order.OrderService;
import com.wuyan.vo.*;
import org.apache.commons.collections.CollectionUtils;
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

    @Reference(interfaceClass = OrderService.class,check = false)
    OrderService orderService;

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
        hallInfoVo.setSoldSeats(orderService.getSoldSeatsByFieldId(fieldId));

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

    /*  ===========================================================================================================
    */
    /**
     * 根据查询条件获取影院列表
     * */
    @Override
    public CinemaListVo cinemaList(CinemaQueryVo cinemaQueryVo) {
        CinemaListVo cinemaListVo = new CinemaListVo();

        Page<MtimeCinemaT> page=new Page<>();

        page.setSize(cinemaQueryVo.getPageSize());
        page.setCurrent(cinemaQueryVo.getNowPage());
        page.setSearchCount(true);

        EntityWrapper<MtimeCinemaT> entityWrapper = new EntityWrapper<>();
        if (!cinemaQueryVo.getBrandId().equals(99)){
            entityWrapper.eq("brand_id",cinemaQueryVo.getBrandId());
        }
        if (!cinemaQueryVo.getDistrictId().equals(99)){
            entityWrapper.eq("area_id",cinemaQueryVo.getDistrictId());
        }
        if (!cinemaQueryVo.getHallType().equals(99)){
            entityWrapper.like("hall_ids","#"+cinemaQueryVo.getHallType()+"#");
        }
        List<MtimeCinemaT> cinemaList= mtimeCinemaTMapper.selectPage(page, entityWrapper);
        List<CinemaVo> cinemaVos = mtimeCinemaT2cinemavo(cinemaList);

        cinemaListVo.setData(cinemaVos);
        cinemaListVo.setNowPage(cinemaQueryVo.getNowPage());
        cinemaListVo.setStatus(0);
        int total=0;
        if (page.getTotal()%cinemaQueryVo.getPageSize()==0){
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize());
        }else {
            total=(int) (page.getTotal()/cinemaQueryVo.getPageSize())+1;
        }
        cinemaListVo.setTotalPage(total);
        return cinemaListVo;
    }


    /**
     * 获取影院查询条件
     * @param cinemaConditionQueryVo
     * @return
     */
    @Override
    public CinemaCoditionVo cinemaCodition(CinemaConditionQueryVo cinemaConditionQueryVo) {
        CinemaCoditionVo cinemaCoditionVo = new CinemaCoditionVo();
        CinemaCondition cinemaCondition = new CinemaCondition();

        //查询地区表
        EntityWrapper<MtimeAreaDictT> entityWrapper = new EntityWrapper<>();
        List<MtimeAreaDictT> mtimeAreaDictTS = mtimeAreaDictTMapper.selectList(entityWrapper);

        //将地区表转换成前端接收的vo
        List<AreaVo> areaVos = mtimeAreaDictsT2AreaVos(mtimeAreaDictTS,cinemaConditionQueryVo.getAreaId());
        cinemaCondition.setAreaList(areaVos);

        //查询品牌表
        EntityWrapper<MtimeBrandDictT> brandDictWrapper=new EntityWrapper<>();
        List<MtimeBrandDictT> brandDictTS=mtimeBrandDictTMapper.selectList(brandDictWrapper);

        //将查询的品牌列表转换成前端接收的Vo
        List<BrandVo> brandVos = mtimebrand2BrandVo(brandDictTS, cinemaConditionQueryVo.getBrandId());
        cinemaCondition.setBrandList(brandVos);

        //查询影厅类型--
        EntityWrapper<MtimeHallDictT> hallDictTEntityWrapper=new EntityWrapper<>();
        List<MtimeHallDictT> mtimeHallDictTS = mtimeHallDictTMapper.selectList(hallDictTEntityWrapper);

        //将查询的影厅类型转化成返回类型的vo
        List<HallTypeVo> hallTypeVos = mtimeHallDictT2halltypevo(mtimeHallDictTS, cinemaConditionQueryVo.getHallType());
        cinemaCondition.setHalltypeList(hallTypeVos);

        cinemaCoditionVo.setData(cinemaCondition);
        cinemaCoditionVo.setStatus(0);

        return cinemaCoditionVo;
    }

    private List<HallTypeVo> mtimeHallDictT2halltypevo(List<MtimeHallDictT> mtimeHallDictTS,Integer hallTypeId) {
        ArrayList<HallTypeVo> hallTypeVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtimeHallDictTS)){
            for (MtimeHallDictT mtimeHallDictT:mtimeHallDictTS) {
                HallTypeVo hallTypeVo = new HallTypeVo();
                if (hallTypeId.equals(mtimeHallDictT.getUuid())){
                    hallTypeVo.setActive(true);
                }
                hallTypeVo.setHalltypeId(mtimeHallDictT.getUuid());
                hallTypeVo.setHalltypeName(mtimeHallDictT.getShowName());
                hallTypeVos.add(hallTypeVo);
            }
        }
        return hallTypeVos;
    }

    private List<BrandVo> mtimebrand2BrandVo(List<MtimeBrandDictT> brandDictTS,Integer brandId) {
        ArrayList<BrandVo> brandVos=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(brandDictTS)){
            for (MtimeBrandDictT mtimeBrandDictT :brandDictTS) {
                BrandVo brandVo = new BrandVo();
                if (brandId.equals(mtimeBrandDictT.getUuid())){
                    brandVo.setActive(true);
                }
                brandVo.setBrandId(mtimeBrandDictT.getUuid());
                brandVo.setBrandName(mtimeBrandDictT.getShowName());
                brandVos.add(brandVo);
            }
        }
        return brandVos;
    }

    private List<AreaVo> mtimeAreaDictsT2AreaVos(List<MtimeAreaDictT> mtimeAreaDictTS,Integer areaId) {
        ArrayList<AreaVo> areaVos=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtimeAreaDictTS)){
            for (MtimeAreaDictT mtimeAreaDictT:mtimeAreaDictTS) {
                AreaVo areaVo = new AreaVo();
                if (areaId.equals(mtimeAreaDictT.getUuid())){
                    areaVo.setActive(true);
                }
                areaVo.setAreaId(mtimeAreaDictT.getUuid());
                areaVo.setAreaName(mtimeAreaDictT.getShowName());
                areaVos.add(areaVo);
            }
        }
        return areaVos;
    }

    private List<CinemaVo> mtimeCinemaT2cinemavo(List<MtimeCinemaT> cinemaList) {
        ArrayList<CinemaVo> cinemaVoList=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cinemaList)){
            for (MtimeCinemaT cinemaT:cinemaList){
                CinemaVo cinemaVo=new CinemaVo();
                cinemaVo.setUuid(cinemaT.getUuid());
                cinemaVo.setCinemaName(cinemaT.getCinemaName());
                cinemaVo.setCinemaAddress(cinemaT.getCinemaAddress());
                cinemaVo.setMinimumPrice(cinemaT.getMinimumPrice());
                cinemaVoList.add(cinemaVo);
            }
        }
        return cinemaVoList;
    }

}
