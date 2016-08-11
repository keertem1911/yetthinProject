package util;

import java.util.HashMap;
import java.util.Map;

import com.yetthin.web.common.RedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JTdoa {
	private boolean logined=false;//初始false登陆成功回调里改为true
	private boolean connected=false;//初始false连接成功回调里改为true;
	
	private static JedisPool poolM=RedisUtil.getInstanceMsater();
	
	private static Jedis jedis_M=null;
	static{
		jedis_M=poolM.getResource();
	}
	public JTdoa() {
		  	 
//		// TODO Auto-generated constructor stub
 	}
	
	public boolean isLogined() {
		return logined;
	}

	public boolean isConnected() {
		return connected;
	}

	//////////////////
	//////////Call backs 回调函数
	///////////////////////
	void error(long tickId, long code, String message){
		/****************
		 * 错误信息采集函数
		 */
		/*以下代码可自由定义*/
		System.out.println("java");
		System.out.println(message);		
	}
	
	void localError( long tickId, long code, String message){}
	
	void connected(){
		/*****************
		 * 连接服务器成功回调
		 */
		/*以下代码可自由定义*/
		System.out.println("connected");
		this.connected=true;
	}
	
	void disconnected(){
		
	//	JedisPoolUtil.release(pool, jedis_M);
		
	}
	
	void logined( long userId, long loginCode, String message)
	{
		/*****************
		 * 登陆成功回调
		 */
		/*以下代码可自由定义*/
		System.out.println(userId+" "+loginCode+" "+message);
		logined=true;
	}
	/**
	 * level2 买卖方摆单
	 * @param tickId
	 * @param symbol  股票代码
	 * @param exchange 市场
	 * @param currency 货币
	 * @param market
	 * @param side 1为卖 0 为买
	 * @param price
	 * @param size 摆单数量
	 * @param checksum 层数
	 */
	void updateMktDepth( long tickId, String symbol,String exchange,String currency, String market, int side, double price, int size, int checksum)
	{
		/*****************
		 * 摆单报价数据更新推送
		 */
		/*以下代码可自由定义*/
		jedis_M.select(1);
		Map<String, String> map=new HashMap<>();
		map.put(side+":"+checksum, price+":"+size+":"+exchange+":"+currency);
		jedis_M.hmset(symbol+":"+exchange, map);
		
		System.out.println("updateMktDepth ");
	 	System.out.println("symbol = "+symbol+",exchange="+exchange+",market="+market+",currency="+currency+",side="+side+",price="+price+",checksum="+checksum);
	}
	
	void orderStatus( long tickId, Order order, OrderState orderState,Contract contract){}
	
	void tickGeneric( long tickId, String symbol,String secType,String exchange,String currency,int tickType,String L1Value, int size)
	{
		/*****************
		 * L1报价数据更新推送
		 */
		/*以下代码可自由定义*/
		StringBuffer sb=new StringBuffer();
		sb.append("secType="+secType);
		sb.append(",currency="+ currency);
		sb.append(",tickType="+ Integer.toString(tickType));
		sb.append(",L1Value="+ L1Value);
		sb.append(",size="+ Integer.toString(size));
		jedis_M.select(0);
		jedis_M.hset(exchange, symbol,sb.toString());
		
		System.out.println("tickGeneric udpate "+jedis_M.hgetAll(symbol+":"+exchange));
		System.out.println("symbol="+symbol+",secType="+secType+",exchange="+exchange+",currency"+currency+",tickType="+tickType+",L1Value="+L1Value+",size="+size);
	}
	
	void tickPrice( long tickId,String symbol,String secType,String exchange,String currency, double price, String time, long volume)
	{
		/*****************
		 * 分笔数据更新推送
		 */
		/*以下代码可自由定义*/
		System.out.println(tickId+" "+symbol+" "+secType+" "+price+" "+time+" "+volume);		
	}
	void accountStatus( long tickerId, UserFund info){}
	
	void updatePortfolio( Contract contract, String tradeManner, int position, int validSize, double marketPrice,
		double marketValue, double averageCost, double unrealizedPNL, double realizedPNL){}
	void message( int code, String msg){}
	void timerFunction(){}
	void updateClient( String version){}
	void updateSymbol( SymbolStruct symbol, int sendFlag,int flag){}
	void updateMarket(String enStatement, String zhStatement, String type, boolean last){}
	void updateUserAccountState( int state, String secType){}
	
	
	//////////////////////////
	/////////requests 接口请求函数
	///////////////////////////
	public native void TDOACreate();//创建对象
	public native  String TDOAVersion();//获取版本信息
	public native  void		TDOABindUserData();//绑定用户信息
	public native  void     TDOAUserData();//请求用户信息
	public native  void		TDOADestory();//销毁对象
	public native  void		TDOADisconnect();//断开连接
	public native  void		TDOASetCallback();//设置回调函数列表
	/********** 以下所有int返回值 0表示请求发送失败 1表示请求发送成功 *****************/
	public native  int		TDOAConnect(String addr, int port);//发送连接服务器请求
	public native  int		TDOAConnectUpdate( String addr, int port, String clientVersion);//发送更新连接请求
	public native  int		TDOALoginServer( String user, String pwd, String mac, int channel, String network);//登陆服务器请求
	public native  int		TDOACreateUser( String user, String pwd,
		String chineseName, String phone, String identity, String mac, int flag);//注册用户请求	
	public native  int		TDOAUpdatePassword( String userName, String oldPassword, String newPassword, int typeChannel);//修改密码
	public native  int		TDOAReqPortfolio();
	public native  int		TDOAReqAccountState( long tickId);
	public native  long      TDOAGetOrderId( int number,long orderid,int flag);// std::vector<long> & orderIds flag 初始0，依次增加，返回-1后从0继续轮询
	public native  int		TDOAPlaceOrder( long tickId, Contract contract,Order order);//const Contract * contract, const Order * order
	public native  int		TDOABachOrder(OrderCombination orderCom,int flag);//const std::vector<OrderCombination>& orderVector
	public native  int		TDOACancelOrder( long tickId, long orderId, String exchange);
	public native  int		TDOABachCancelOrder(CancelOrderData cancelData,int flag);//const std::vector<CancelOrderData> & cancelOrderData
	public native  int		TDOASubscribeOrderState();
	public native  int		TDOASubscribeMarketDepth( long tickId, Contract contract, int numRows);//订阅摆单报价数据推送请求
	public native  int       TDOASubscibeLevel1Data( long tickId,Contract contract);//订阅L1报价数据更新推送请求
	public native  int		TDOASubscribeMarketData( long tickId,Contract contract);//订阅分笔成交数据更新推送请求
	public native  int		TDOASubscribeNews( long tickId);
	public native  int		TDOACancelMarketDepth( long tickId, String channel);//channel 取值 sz sh或者 SZ SH 取消请求
	public native  int       TDOACancelLevel1Data( long tickId, String channel );
	public native  int		TDOACancelMarketData( long tickId, String channel);

	public native  int		TDOAReqHistoryPlacedOrder( long tickId,long beginTime,long endTime);
	public native  int		TDOAReqHistoryFilledOrder( long tickId,long beginTime,long endTime);
	public native  int		TDOAReqDayPlacedOrder( long tickId);
	public native  int		TDOAReqDayFilledOrder( long tickId);
	public native  int		TDOAReqDeliveryOrder( long tickId,long beginTime,long endTime);
	public native  int		TDOAReqRQPos( long tickId);

	public native  int		TDOAReqRiskParam(); //请求风险参数
	public native  int		TDOAReqFee();//请求各种费用

	public native  int		TDOAReqSymbolQuery( long time);//股票搜索

	public native  int		TDOATimerFunction();

	public native  int		TDOARecvMessage( String text, String phone, String email, String qq);
	
	public native  int		TDOAReqProfitList( long tickId);//请求盈利列表
    static {
        System.loadLibrary("JTdoa");//
    }
	public static void main(String[] args) {
		JTdoa jda=new JTdoa();
		jda.TDOACreate();
		jda.TDOAConnect("222.173.29.210", 7005);
		while(jda.connected==false)
		{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(jda.connected);
		}
		int ls=jda.TDOALoginServer("td4", "td4", "90-b1-1c-80-a4-78", 0, "1");
		System.out.println(ls);
		while(jda.logined==false)
		{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(jda.logined);
		}
		
		Contract contract=new Contract();
		contract.symbol="603737"; 
		contract.currency="CNY"; 
		contract.exchange="SH";
		contract.secType="STK";
		System.out.println("hello");
		int status=jda.TDOASubscribeMarketDepth(4, contract, 5);
		System.out.println(status);
		System.out.println("hello");
		
		status=jda.TDOASubscribeMarketData(5, contract);
		System.out.println("mktdata"+status);
		System.out.println("hello");
		
		status=jda.TDOASubscibeLevel1Data(6, contract);
		System.out.println("mktdata"+status);
		System.out.println("tickPrice");		
		
	}

}
