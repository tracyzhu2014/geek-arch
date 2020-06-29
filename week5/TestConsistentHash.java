import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 *编写测试用例，测试算法：100万KW, 10个服务器节点下，数据在服务器的分布情况。
 * 计算数据分布的期望和方差。
 */
public class TestConsistentHash{

    int m_allcount ; // 测试数据量
    int m_virtualCount; //虚拟节点数
    LinkedList<String> m_nodes;  //各主机节点名
    private Map<String,Integer> m_result;  //各主机节点的数据量

    public TestConsistentHash()
    {
        m_result = new HashMap<String,Integer>();
        LinkedList<String> nodes = new LinkedList<String>();
        m_nodes.add("192.168.2.1:8080");
        m_nodes.add("192.168.2.2:8080");
        m_nodes.add("192.168.2.3:8080");
        m_nodes.add("192.168.2.4:8080");
        m_nodes.add("192.168.2.5:8080");
        m_nodes.add("192.168.2.6:8080");
        m_nodes.add("192.168.2.7:8080");
        m_nodes.add("192.168.2.8:8080");
        m_nodes.add("192.168.2.9:8080");
        m_nodes.add("192.168.2.10:8080");
    }

    /**标准差σ=sqrt(s^2)
     *
     */
    public double standardDiviation(int allNum) {
        int size = m_result.size();
        double dAve=allNum/size;//求平均值
        double dVar=0;
        for (Integer iTemp : m_result.values()){
            double dTemp = iTemp.doubleValue() - dAve;
            dVar+= dTemp * dTemp;
        }
        return Math.sqrt(dVar/size);
    }

    /**
     * 运行一次，则统计出指定KV数据量，和指定虚拟节点情况下的数据分布
     * @param allCount  测试数据量
     * @param virtualCount 虚拟节点数
     */
    public  void processTest(int allCount, int virtualCount){
        m_allcount = allCount;
        m_virtualCount = virtualCount;
        m_result.clear();

        ConsistentHashLoadBalance consistentHash = new ConsistentHashLoadBalance(m_nodes, virtualCount);
        consistentHash.printVirtualNode();

        //测试随机key值的分布情况
        Random rnd = new Random();
        for (int i = 0; i < allCount; i++) {
            String tempKey = "" + rnd.nextLong();
            String server = consistentHash.selectNode(tempKey);
            String realServer = server.split("vn")[0];
            //System.out.println("[" + tempKey + "]的hash值为"+ consistentHash.hash(tempKey) + ", 被路由到虚拟结点[" + server+ "], 真实结点[" + realServer + "]");
            m_result.put(realServer,(m_result.get(realServer)==null?0:m_result.get(realServer))+1);
        }
        m_result.forEach((k,v)->{
            System.out.println("结点["+k+"]上有"+v);
        });
        System.out.println("数据分布标准差为:" + standardDiviation(allCount));
    }


    public static void main(String[] args){
        //初始化中设定10台主机
        TestConsistentHash oTest = new TestConsistentHash();

        //100万数据量时，设定10个虚拟节点的数据分布情况
        oTest.processTest(1000000, 10);

        //100万数据量时，设定100个虚拟节点的数据分布情况
        oTest.processTest(1000000, 100);

        //100万数据量时，设定1000个虚拟节点的数据分布情况
        oTest.processTest(1000000, 1000);
    }

}