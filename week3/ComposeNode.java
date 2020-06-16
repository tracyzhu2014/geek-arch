import java.util.ArrayList;
import java.util.List;

/**
组合模式
 */
public class ComposeNode{
    private String m_nodeName;
    private String m_nodeType;

    private List<ComposeNode> m_subNodes = new ArrayList<ComposeNode>();

    public ComposeNode(String name, String type){
        m_nodeName = name;
        m_nodeType = type;
    }

    /* 加入子节点*/
    public void addSubNode(ComposeNode oNode){
        m_subNodes.add(oNode);
    }

    /*
     递归遍历节点信息
     */
    public void printAllNode(){
        System.out.println("Type=" + m_nodeType + ",Name=" + m_nodeName);
        for ( ComposeNode oTempNode : m_subNodes ){
            oTempNode.printAllNode();
        }
    }

    /*
    绘制图形
     */
    private static ComposeNode createWindow()
    {
        ComposeNode oWindows = new ComposeNode("main","window");
        ComposeNode oLogo = new ComposeNode("logo","picture");
        oWindows.addSubNode(oLogo);
        ComposeNode oLogin = new ComposeNode("登录","button");
        oWindows.addSubNode(oLogin);
        ComposeNode oCreate = new ComposeNode("注册","button");
        oWindows.addSubNode(oCreate);
        /* 绘制主输入frame*/
        ComposeNode oMainInput = new ComposeNode("maininput","frame");

        ComposeNode oNode = new ComposeNode("用户名","label");
        oMainInput.addSubNode(oNode);
        ComposeNode oUsername = new ComposeNode("username","textbox");
        oMainInput.addSubNode(oUsername);
        ComposeNode oPL = new ComposeNode("密码","label");
        oMainInput.addSubNode(oPL);
        ComposeNode oPasswd = new ComposeNode("password","passwordbox");
        oMainInput.addSubNode(oPasswd);
        ComposeNode oCheck = new ComposeNode("remember","checkbox");
        oMainInput.addSubNode(oCheck);
        ComposeNode oRemL = new ComposeNode("记住用户名","label");
        oMainInput.addSubNode(oPasswd);
        ComposeNode olinklabel = new ComposeNode("忘记密码","linklabel");
        oMainInput.addSubNode(olinklabel);

        oWindows.addSubNode(oMainInput);
        return oWindows;
    }

    public static void main(String[] args) {

        //创建图形
        ComposeNode oWindows = createWindow();
        //遍历输出
        oWindows.printAllNode();

    }


}
