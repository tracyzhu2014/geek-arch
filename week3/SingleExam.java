/**
 * 单例:
 * 重点：
 * 1）构造函数为private，避免外部可以创建实例
 * 2）new实例时，考虑线程安全问题
 * 3）是否延迟加载
 */
pubic class SingleExam {
    private static SingleExam() {}
    private static SingleExam g_instance = null ;
    public static SingleExam getInstance(){
         if ( null == g_instance) {
             synchronized (SingleExam.class){
                 g_instance = new SingleExam();
             }
         }
         return g_instance;
    }
}