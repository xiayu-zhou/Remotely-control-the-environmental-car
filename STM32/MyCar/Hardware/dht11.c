#include "stm32f10x.h"                  // Device header
#include "Delay.h"


#define DHT_GPIO_PORT		GPIOB
#define DHT_GPIO_PIN		GPIO_Pin_11
#define DHT_RCC_PORT		RCC_APB2Periph_GPIOB
/**
  * @brief  DHT11_GPIO初始化函数
  * @param  Mode：指定输入或输出模式
  * @return None
  */
void DHT_GPIO_Init(GPIOMode_TypeDef Mode)
{
	GPIO_InitTypeDef GPIO_InitStructure;
	
	RCC_APB2PeriphClockCmd(DHT_RCC_PORT,ENABLE);
	
	GPIO_SetBits(DHT_GPIO_PORT,DHT_GPIO_PIN);
	
	GPIO_InitStructure.GPIO_Mode = Mode;
	GPIO_InitStructure.GPIO_Pin = DHT_GPIO_PIN;
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	
	GPIO_Init(DHT_GPIO_PORT,&GPIO_InitStructure);
}

/**
  * @brief  DHT11模块起始信号函数
  * @param  None
  * @return 1或0，标志起动信号成功与否
  */
uint8_t DHT_Start(void)
{
	//推挽输出模式
	DHT_GPIO_Init(GPIO_Mode_Out_PP);							//输出模式
	
	//主机拉低总线（时间需要大于18ms）后设置为输入并释放数据总线，等待从机（DHT11）响应
	GPIO_ResetBits(DHT_GPIO_PORT,DHT_GPIO_PIN);		//输出20ms低电平后拉高
	//延迟大于18ms避免误差
	Delay_ms(20);
	//拉高总线等待
	GPIO_SetBits(DHT_GPIO_PORT,DHT_GPIO_PIN);
	
	//这里使用浮空输入是为了等待从机的响应，
	//	如果从机响应后会将总线拉低80us，拉高80us通知主机准备接收数据
	DHT_GPIO_Init(GPIO_Mode_IN_FLOATING);					//输入模式
	Delay_us(20);
	
	//如果从机拉低总线进入判断语句
	if(!GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN))
	{
		//如果从机拉低总线进入循环语句，因为拉低80us所以还是低电平，高电平退出
		while(!GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN));
		//当主机拉高电平循环，等待数据发送前拉低50us，如果开始发送数据这个循环将会断开
		while(GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN));
		return 1;
	}
	return 0;
}

/**
  * @brief  接收DHT11发送来8位的数据
  * @param  None
  * @return 返回接收到的8位数据
  */
uint8_t DHT_Get_Byte_Data(void)
{
	uint8_t temp;
	int i;
	for(i = 0; i < 8; i++)
	{
		temp <<= 1;
		while(!GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN));
		Delay_us(28);
		GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN) ? (temp |= 0x01) : (temp &= ~0x01);
		while(GPIO_ReadInputDataBit(DHT_GPIO_PORT,DHT_GPIO_PIN));
	}
	return temp;
}

/**
  * @brief  获取DHT11的温度湿度数据
  * @param  buffer[]：需要传入一个存储数据的数组
  * @return 返回数据校验是否正确 1：正确 0：失败
  */
uint8_t DHT_Get_Temp_Humi_Data(uint8_t buffer[])
{
	if(DHT_Start())
	{
		buffer[0] = DHT_Get_Byte_Data();
		buffer[1] = DHT_Get_Byte_Data();
		buffer[2] = DHT_Get_Byte_Data();
		buffer[3] = DHT_Get_Byte_Data();
		buffer[4] = DHT_Get_Byte_Data();
	}
	return (buffer[0]+buffer[1]+buffer[2]+buffer[3] == buffer[4]) ? 1 : 0;
}
