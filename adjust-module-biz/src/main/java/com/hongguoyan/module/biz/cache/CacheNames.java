package com.hongguoyan.module.biz.cache;

/**
 * Biz cache names (Spring Cache).
 *
 * <p>注意：本项目缓存统一走 Redis TTL 过期，不做主动失效。
 */
public interface CacheNames {

    String AREA_LIST = "biz:area:list";

    String SCHOOL_SIMPLE_ALL = "biz:school:simple-all";
    String SCHOOL_TREE = "biz:school:tree";
    String SCHOOL_OVERVIEW = "biz:school:overview";
    String SCHOOL_ADJUSTMENT_PAGE = "biz:school:adjustment:page";

    String MAJOR_LEVEL1_LIST = "biz:major:level1-list";
    String MAJOR_LIST = "biz:major:list";
    String MAJOR_TREE = "biz:major:tree";

    String PROJECT_CONFIG = "biz:project-config:get";

    String ADJUSTMENT_UPDATE_STATS = "biz:adjustment:update-stats";
    String ADJUSTMENT_HOT_RANKING_PAGE = "biz:adjustment:hot-ranking:page";
    String ADJUSTMENT_DETAIL_ROWS = "biz:adjustment:detail:rows";

    String ADJUSTMENT_ADMIT_LIST = "biz:adjustment:admit-list";

    String UNDERGRADUATE_MAJOR_LIST = "biz:undergraduate-major:list";

    String SCHOOL_RANK_LIST = "biz:school-rank:list";
}

