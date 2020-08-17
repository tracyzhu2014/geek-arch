### 2 分析如下HiveQL，生成的MapReduce执行程序，map函数输入是什么？输出是什么，reduce函数输入是什么？输出是什么？
在map阶段，map函数同时读取两个表的每一行数据，为了区分两种来源的key/value数据对，对每条数据打一个标签。
map函数的输入：Page_view表和user表结构的每一行数据为map的输入数据
map输出： 每行数据的key为userid, 
而Page_view表的value为：Page_view#pageid,time
user表的value为：user#age,gender

系统通过shuffle将相同的userid聚合在同一个reduce中。在reduce阶段对相同的userid进行join连接操作，输出page,age