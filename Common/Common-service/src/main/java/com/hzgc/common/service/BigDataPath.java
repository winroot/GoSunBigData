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

    /**
     * DynRepo模块请求路径
     */
    public static final String DYNREPO_CAPTURENUM = "/dynRepo/CaptureNumber";
    public static final String DYNREPO_CAPTURENUM_SEARCHDYNREPO = "/dynaicNumberService";
    public static final String DYNREPO_CAPTURENUM_SEARCHSTAREPO = "/staticNumberService";
    public static final String DYNREPO_CAPTURENUM_FILTER = "/timeSoltNumber";
    public static final String DYNREPO_CAPTUREPICTURESEARCH = "/dynRepo/CapturePictureSearch";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_SEARCH = "/search";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_SEARCHRESULT = "/getSearchResult";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_ATTRIBUTE = "/getAttribute";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_COUNT = "/captureCountQuery";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_COUNTS = "/getCaptureNumber";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_HISTORY = "/getCaptureHistory";
    public static final String DYNREPO_CAPTUREPICTURESEARCH_ATTRIBUTECOUNT = "/captureAttributeQuery";

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
    public static final String STAREPO_GET = "/getObjectInfo";
    public static final String STAREPO_SEARCH_BYROWKEY = "/searchByRowkey";
    public static final String STAREPO_GETSEARCHRESULT= "/getRocordOfObjectInfo";
    public static final String STAREPO_GETSEARCHPHOTO = "/getSearchPhoto";

}
