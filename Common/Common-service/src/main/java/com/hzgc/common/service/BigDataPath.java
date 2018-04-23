package com.hzgc.common.service;

/**
 * 大数据接口请求路径
 */
public class BigDataPath {

    /**
     * 大数据路径
     */
    public static final String ROOT = "/bigData";

    /**
     * Clustering模块请求路径
     */
    public static final String CLUSTERING = "/clustering";
    public static final String CLUSTERING_SEARCH = "/clusteringSearch";
    public static final String CLUSTERING_DETAILSEARCH = "/detailClusteringSearch";
    public static final String CLUSTERING_DETAILSEARCH_V1 = "/detailClusteringSearch_v1";
    public static final String CLUSTERING_DELETE = "/deleteClustering";
    public static final String CLUSTERING_IGNORE = "/ignoreClustering";

    /**
     * Device模块请求路径
     */
    public static final String DEVICE = "/device";
    public static final String DEVICE_BIND = "/bindDevice";
    public static final String DEVICE_UNBIND = "/unbindDevice";
    public static final String DEVICE_RENAMENOTES = "/renameNotes";

    public static final String WARNRULE = "/warnRule";
    public static final String WARNRULE_CONFIG = "/configRules";
    public static final String WARNRULE_ADD = "/addRules";
    public static final String WARNRULE_GETCOMPARE = "/getCompareRules";
    public static final String WARNRULE_DELETE = "/deleteRules";
    public static final String WARNRULE_OBJECTTYPE_GET = "/objectTypeHasRule";
    public static final String WARNRULE_OBJECTTYPE_DELETE = "/deleteObjectTypeOfRules";

    /**
     * DynRepo模块请求路径
     */
    public static final String CAPTURENUM = "/CaptureNumber";
    public static final String CAPTURENUM_SEARCHDYNREPO = "/dynaicNumberService";
    public static final String CAPTURENUM_SEARCHSTAREPO = "/staticNumberService";
    public static final String CAPTURENUM_FILTER = "/timeSoltNumber";

    public static final String CAPTUREPICTURESEARCH = "/CapturePictureSearch";
    public static final String CAPTUREPICTURESEARCH_SEARCH = "/search";
    public static final String CAPTUREPICTURESEARCH_SEARCHRESULT = "/getSearchResult";
    public static final String CAPTUREPICTURESEARCH_ATTRIBUTE = "/getAttribute";
    public static final String CAPTUREPICTURESEARCH_COUNT = "/captureCountQuery";
    public static final String CAPTUREPICTURESEARCH_COUNTS = "/getCaptureNumber";
    public static final String CAPTUREPICTURESEARCH_HISTORY = "/getCaptureHistory";
    public static final String CAPTUREPICTURESEARCH_ATTRIBUTECOUNT = "/captureAttributeQuery";

    /**
     * Face模块请求路径
     */
    public static final String FACE = "/face";
    public static final String FEATURE_EXTRACT = "/featureExtract";

    /**
     * StaRepo模块请求路径
     */
    public static final String STAREPO = "/staRepo";
    public static final String STAREPO_ADD = "/addObjectInfo";
    public static final String STAREPO_DELETE = "/deleteObjectInfo";
    public static final String STAREPO_UPDATE = "/updateObjectInfo";
    public static final String STAREPO_GET_OBJECTINFO = "/getObjectInfo";
    public static final String STAREPO_GET_PHOTOBYKEY = "/getPhotoByKey";
    public static final String STAREPO_SEARCH_BYROWKEY = "/searchByRowkey";
    public static final String STAREPO_GETSEARCHRESULT= "/getRocordOfObjectInfo";
    public static final String STAREPO_GETSEARCHPHOTO = "/getSearchPhoto";

}
