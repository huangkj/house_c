package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17/017.
 */

public class RobotBean {

    /**
     * msgtype : {"choice":{"items":[{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}],"title":"列表的标题"}}
     */

    public MsgtypeBean msgtype;

    public static class MsgtypeBean {
        /**
         * choice : {"items":[{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}],"title":"列表的标题"}
         */

        public static class ChoiceBean {
            /**
             * items : [{"id":"xxx","name":"1.列表内容"},{"id":"xxx","name":"2.列表内容"}]
             * title : 列表的标题
             */

            public String title;
            public List<ItemsBean> items;

            public static class ItemsBean {
                /**
                 * id : xxx
                 * name : 1.列表内容
                 */

                public String id;
                public String name;
            }
        }
    }
}
