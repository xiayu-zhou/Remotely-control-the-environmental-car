#include "stm32f10x.h"
#include "PWM.h"
#include "MY_TIME.h"
#include "delay.h"	
#include "OLED.h"

extern uint8_t TH_Flag;
extern uint8_t DeferenceLight;
uint8_t LightFlag = 0;


int main(void)
{

	uint8_t com1 = 90,com2 = 90;
	GPIO_InitTypeDef GPIO_Inistructure;	//关于GPIO口的结构体定义,初始化GPIO用的
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB,ENABLE);	//用来开启或关闭APB2外设时钟
	GPIO_Inistructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;	//设置为浮空输入
	GPIO_Inistructure.GPIO_Pin = GPIO_Pin_12|GPIO_Pin_13|GPIO_Pin_14|GPIO_Pin_15;
	GPIO_Inistructure.GPIO_Speed = GPIO_Speed_50MHz;	//指定所选引脚的速度
	
	GPIO_Init(GPIOB,&GPIO_Inistructure);
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);
	GPIO_Inistructure.GPIO_Mode =GPIO_Mode_Out_PP;	
	GPIO_Inistructure.GPIO_Pin = GPIO_Pin_5|GPIO_Pin_6|GPIO_Pin_7|GPIO_Pin_8;
	GPIO_Inistructure.GPIO_Speed = GPIO_Speed_50MHz;
	
	GPIO_Init(GPIOA,&GPIO_Inistructure);	//根据指定的参数初始化GPIOx外围设备 ，GPIO_InitStruct中的参数 

	GPIO_ResetBits(GPIOB,GPIO_Pin_12);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_13);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_14);		//默认低电平
	GPIO_ResetBits(GPIOB,GPIO_Pin_15);		//默认低电平
	GPIO_ResetBits(GPIOA,GPIO_Pin_8);		//默认低电平
	GPIO_SetBits(GPIOA,GPIO_Pin_5);		//蜂鸣器默认高电平默认低电平
	
	PWM_Init();
	
	//警报定时器的初始化
	Time_Init();
	
	OLED_Init();
	
	//初始化舵机角度
	PWM_SetCompare1(com1);
	PWM_SetCompare2(com2);
	while(1)
	{	
		OLED_ShowNum(1,1,LightFlag,2);
		//GPIO_SetBits(GPIOA,GPIO_Pin_8);
		//引脚是小车PWM舵机1低头	0001
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == SET)){
				
			if(com1<=0) com1++;
			if(com1 >= 132)com1--;
				
			if(com1 > 0 && com1 < 132)
				PWM_SetCompare1(++com1);
			delay_ms(40);
		}
		//引脚是小车PWM舵机1抬头	0010
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == RESET)){
			if(com1<=0) com1++;
			if(com1 >= 132)com1--;
				
			if(com1 > 0 && com1 < 180)
				PWM_SetCompare1(--com1);
			delay_ms(40);
		}

		//PWM舵机2		0011
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == SET)){
			if(com1<=0) com2++;
			if(com1 >= 180)com2--;
			if(com2 > 0 && com2 < 180)
				PWM_SetCompare2(++com2);
			delay_ms(40);
		}
		//PWM舵机2		0100
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == RESET)){
			if(com1<=0) com2++;
			if(com1 >= 180)com2--;
			if(com2 > 0 && com2 < 180)
				PWM_SetCompare2(--com2);
			delay_ms(40);
		}	
			
		//开启灯光		0101
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == SET)){
			LightFlag = 1;
		}
		
		//关闭灯光		0110
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == RESET)){
			LightFlag = 0;
		}
			
		
		//打开警报灯	0111
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == SET)){
			GPIO_ResetBits(GPIOA,GPIO_Pin_5);
			TIM_Cmd(TIM3,ENABLE);	
		}
			
		//关闭警报灯	1000
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == RESET)){
			TIM_Cmd(TIM3,DISABLE);	
			GPIO_SetBits(GPIOA,GPIO_Pin_5);
		}
			
		//探头复位		1001
		if((GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_15) == SET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_14) == RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_13)== RESET)&&
			(GPIO_ReadInputDataBit(GPIOB,GPIO_Pin_12) == SET)){
			com1 = com2 = 90;
			PWM_SetCompare1(com1);	
			PWM_SetCompare2(com2);
		}
		
		if(LightFlag == 1){
			GPIO_SetBits(GPIOA,GPIO_Pin_8);
		}else{
			GPIO_ResetBits(GPIOA,GPIO_Pin_8);		
		}
		
		if(TH_Flag != 1){
			
			if(DeferenceLight == 0)
				GPIO_SetBits(GPIOA,GPIO_Pin_6);
			else 
				GPIO_SetBits(GPIOA,GPIO_Pin_7);
		}
		else{	
			if(DeferenceLight == 0)
				GPIO_ResetBits(GPIOA,GPIO_Pin_6);
			else
				GPIO_ResetBits(GPIOA,GPIO_Pin_7);
		}
	}
}


