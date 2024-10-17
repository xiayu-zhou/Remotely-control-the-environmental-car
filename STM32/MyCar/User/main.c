#include "stm32f10x.h"                  // Device header
#include "Delay.h"
#include "main.h"
#include "CHUANKOU.h"
#include "HC_SR04.h"
#include "OLED.h"
#include "dht11.h"
#include "Motor.h"
int left = 0,rigt = 0;

char AllData[30];
float length;


int main(void)
{
	//OLED屏幕的初始化
	OLED_Init();
	//车的GPIO口初始化
	//GPIO_Car_Init();
	//车的初始化
	Motor_Init();
	//串口初始化
	CHUANKOU_Send_Init();
	//超声波初始化
	HC_SR04_Init();
	//从机GPIO功能初始化
	GPIO_Function_Init();
	OLED_ShowString(1, 1, "Kiana I L Y");
	
	//
	while (1)
	{
		GPIO_SetBits(GPIOB,GPIO_Pin_4);
		OLED_ShowString(4,1,Rx);
		if( Getflag() == SET)
		{
			if(Rx[4] != '!')
				Function_Star(Rx[4]);
			
			if(Rx[0] != '!' && Rx[1] != '!'){
				if(Rx[0]!='F'){
					Car_able_R(Rx[0]);
				}
				if(Rx[1]!='F'){
					Car_able_L(Rx[1]);
				}
			}else if(Rx[0] == '!' && Rx[1] == '!'){
				free_Action_s(Rx[2],Rx[3]);
			}
		}
	}
}

void Function_Star(char fun)
{
	switch(fun)
	{
		case '+':
		{
			DH_Show();
			Rx[4] = '!';	
		}break;
		case '0':
		{
			//0001
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '1':
		{
			//0001
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_SetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';			
		}break;
		case '2':
		{
			//0010
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_SetBits(GPIOB,GPIO_Pin_13);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_12);	
			Rx[4] = '!';
		}break;
		case '3':
		{
			//0011
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_SetBits(GPIOB,GPIO_Pin_13);		
			GPIO_SetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '4':
		{
			//0100
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_SetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '5':
		{
			//0101
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_SetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_SetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '6':
		{
			//0110
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_SetBits(GPIOB,GPIO_Pin_14);		
			GPIO_SetBits(GPIOB,GPIO_Pin_13);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '7':
		{
			//0111
			GPIO_ResetBits(GPIOB,GPIO_Pin_15);		
			GPIO_SetBits(GPIOB,GPIO_Pin_14);		
			GPIO_SetBits(GPIOB,GPIO_Pin_13);	
			GPIO_SetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '8':
		{
			//1000
			GPIO_SetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
		case '9':
		{
			//1001
			GPIO_SetBits(GPIOB,GPIO_Pin_15);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_14);		
			GPIO_ResetBits(GPIOB,GPIO_Pin_13);		
			GPIO_SetBits(GPIOB,GPIO_Pin_12);
			Rx[4] = '!';
		}break;
	}
	delay_ms(10);
}

void GPIO_Function_Init()
{
	GPIO_InitTypeDef GPIO_Inistructure;	//关于GPIO口的结构体定义,初始化GPIO用的
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB,ENABLE);	//用来开启或关闭APB2外设时钟
	GPIO_Inistructure.GPIO_Mode = GPIO_Mode_Out_PP;	//设置为浮空输入
	GPIO_Inistructure.GPIO_Pin = GPIO_Pin_12|GPIO_Pin_13|\
	GPIO_Pin_14|GPIO_Pin_15;
	GPIO_Inistructure.GPIO_Speed = GPIO_Speed_50MHz;	//指定所选引脚的速度
	GPIO_Init(GPIOB,&GPIO_Inistructure);
	GPIO_ResetBits(GPIOB,GPIO_Pin_12);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_13);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_14);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_15);		//默认低电平
}

void DH_Show()
{
	char str[20];
	uint8_t DHT_Buffer[5];
	if(DHT_Get_Temp_Humi_Data(DHT_Buffer))
	{
		sprintf(str,"temperature:%2d.%1d",DHT_Buffer[2],DHT_Buffer[3]);
		OLED_ShowString(2,1,(uint8_t *)str);
		sprintf(str,"humidity:%2d.%1d",DHT_Buffer[0],DHT_Buffer[1]);
		OLED_ShowString(3,1,(uint8_t *)str);
	}
	length = Get_Length();
	sprintf(AllData,"@%2d.%1d#%2d.%1d#%d*",DHT_Buffer[2],DHT_Buffer[3],DHT_Buffer[0],DHT_Buffer[1],(int)length);
	Send_String(AllData);
}

void free_Action_s(char L,char R)
{
	switch(R)
	{
		case 'N':
		Car_Right(-20);
		break;
		case 'M':
		Car_Right(20);
		break;	
		case 'A':
		Car_Right(-100);
		break;
		case 'B':
		Car_Right(-90);
		break;
		case 'C':
		Car_Right(-75);
		break;
		case 'D':
		Car_Right(-60);
		break;
		case 'E':
		Car_Right(-40);
		break;
		case 'F':
		Car_Right(0);
		break;
		case 'G':
		Car_Right(40);
		break;
		case 'H':
		Car_Right(60);
		break;
		case 'I':
		Car_Right(75);
		break;
		case 'J':
		Car_Right(90);
		break;
		case 'K':
		Car_Right(100);
		break;
	}
	
	switch(L)
	{
		case 'N':
		Car_Left(-20);
		break;
		case 'M':
		Car_Left(20);
		break;	
		case 'A':
		Car_Left(-100);
		break;
		case 'B':
		Car_Left(-90);
		break;
		case 'C':
		Car_Left(-75);
		break;
		case 'D':
		Car_Left(-60);
		break;
		case 'E':
		Car_Left(-40);
		break;
		case 'F':
		Car_Left(0);
		break;
		case 'G':
		Car_Left(40);
		break;
		case 'H':
		Car_Left(60);
		break;
		case 'I':
		Car_Left(75);
		break;
		case 'J':
		Car_Left(90);
		break;
		case 'K':
		Car_Left(100);
		break;
	}
}

void Car_able_R(char r){

	switch(r)
	{
		//右履带向前
		case '0':
		{
			rigt = 100;
			Car_Right(rigt);
		}
		break;
		//右履带向后
		case '1':
		{
			rigt = -100;
			Car_Right(rigt);
		}
		break;
		//右履停止
		case '2':
		{
			rigt = 0;
			Car_Right(rigt);
		}
		break;
		
	}
}

void Car_able_L(char lef){
	switch(lef)
	{
		//左履带向前
		case '0':
		{
			left = 100;
			Car_Left(left);
		}
		break;
		//左履带向后
		case '1':
		{
			left = -100;
			Car_Left(left);
		}
		break;
		//左履停止
		case '2':
		{
			left = 0;
			Car_Left(left);
		}
		break;
	}
}

