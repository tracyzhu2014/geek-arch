import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 带虚拟节点的一致性哈希算法
 */
public class ConsistentHashLoadBalance {

    private TreeMap<Long, String> virtualNodes = new TreeMap<>();
    private LinkedList<String> nodes;        //每个真实节点对应的虚拟节点数
    private final int replicCnt;


    public ConsistentHashLoadBalance(LinkedList<String> nodes, int replicCnt){
        this.nodes = nodes;
        this.replicCnt = replicCnt;
        initalization();
    }

    /**
     * 初始化哈希环
     * 循环计算每个node名称的哈希值，将其放入treeMap
     */
    private void initalization(){
        for (String nodeName: nodes) {
            for (int i = 0; i < replicCnt/nodes.size(); i++) {
                String virtualNodeName = getNodeNameByIndex(nodeName, i);
                virtualNodes.put(hash(virtualNodeName), nodeName);
            }
        }
    }

   
    private String getNodeNameByIndex(String nodeName, int index){
        return new StringBuffer(nodeName)
                .append("&&")
                .append(index)
                .toString();
    }

    /**
     * 根据资源key选择返回相应的节点名称
     * @param key
     * @return 节点名称
     */
    public String selectNode(String key){
        Long hashOfKey = hash(key);
        if (! virtualNodes.containsKey(hashOfKey)) {
            Map.Entry<Long, String> entry = virtualNodes.ceilingEntry(hashOfKey);
            if (entry != null)
                return entry.getValue();
            else
                return nodes.getFirst();
        }else
            return virtualNodes.get(hashOfKey);
    }


    private Long hash(String nodeName) {
        byte[] digest = md5(nodeName);
        return (((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF))
                & 0xFFFFFFFFL;
    }


    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public byte[] md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(str.getBytes("UTF-8"));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNode(String node){
        nodes.add(node);
        String virtualNodeName = getNodeNameByIndex(node, 0);
        for (int i = 0; i < replicCnt/nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                virtualNodes.put(hash(virtualNodeName), node);
            }
        }
    }

    public void removeNode(String node){
        nodes.remove(node);
        String virtualNodeName = getNodeNameByIndex(node, 0);
        for (int i = 0; i < replicCnt/nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                virtualNodes.remove(hash(virtualNodeName), node);
            }
        }
    }

    public void printVirtualNode()
    {
        System.out.println("------hostcount:" + nodes.size() + ",virtualcount:"+ virtualNodes.size() + "------");
    }
    private void printTreeNode(){
        if (virtualNodes != null && ! virtualNodes.isEmpty()){
            virtualNodes.forEach((hashKey, node) ->
                    System.out.println(
                            new StringBuffer(node)
                                    .append(" ==> ")
                                    .append(hashKey)
                    )
            );
        }else
            System.out.println("Cycle is Empty");

    }

    public static void main(String[] args){
        LinkedList<String> nodes = new LinkedList<String>();
        nodes.add("192.168.2.1:8080");
        nodes.add("192.168.2.2:8080");
        nodes.add("192.168.2.3:8080");
        nodes.add("192.168.2.4:8080");
        nodes.add("192.168.2.5:8080");
        nodes.add("192.168.2.6:8080");
        nodes.add("192.168.2.7:8080");
        nodes.add("192.168.2.8:8080");
        nodes.add("192.168.2.9:8080");
        nodes.add("192.168.2.10:8080");
        ConsistentHashLoadBalance consistentHash = new ConsistentHashLoadBalance(nodes, 10);
        //consistentHash.printTreeNode();

        //测试随机key值的分布情况
        Random rnd = new Random();
        for (int i = 0; i < 1000000; i++) {
            String tempKey = "" + rnd.nextLong();
            String server = consistentHash.selectNode(tempKey);
            String realServer = server.split("vn")[0];
            //System.out.println("[" + tempKey + "]的hash值为"+ consistentHash.hash(tempKey) + ", 被路由到虚拟结点[" + server+ "], 真实结点[" + realServer + "]");
        }
    }
}