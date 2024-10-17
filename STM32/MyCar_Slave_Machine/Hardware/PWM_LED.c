#include "stm32f10x.h"                  // Device header


void PWM_LED_Init(void)
{
	//Time Base初始化结构定义
	TIM_TimeBaseInitTypeDef TIM_TimeBaseInitStructure;
	
	
	TIM_OCInitTypeDef TIM_OCInitStructure;
	
	GPIO_InitTypeDef GPIO_Inistructure;	//关于GPIO口的结构体定义,初始化GPIO用的
	//***********************************************************************
	
	//***********************************************************************
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);	//用来开启或关闭APB2外设时钟
	GPIO_Inistructure.GPIO_Mode = GPIO_Mode_AF_PP;	//GPIO_Mode 模式//
	GPIO_Inistructure.GPIO_Pin = GPIO_Pin_6|GPIO_Pin_7;	//需要配置的GPIO引脚。
	GPIO_Inistructure.GPIO_Speed = GPIO_Speed_50MHz;	//指定所选引脚的速度
	GPIO_Init(GPIOA,&GPIO_Inistructure);	//根据指定的参数初始化GPIOx外围设备 ，GPIO_InitStruct中的参数 
	//***********************************************************************
	
	
	
	//开启RCC TIM2 时钟
	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM3,ENABLE);
	
	//配置TIMx内部时钟 TIM2时钟
	TIM_InternalClockConfig(TIM3);
	
	//指定时钟分频 1分频
	TIM_TimeBaseInitStructure.TIM_ClockDivision = TIM_CKD_DIV1;
	//计数模式 选择向上模式
	TIM_TimeBaseInitStructure.TIM_CounterMode = TIM_CounterMode_Up;
	//周期，ARR自动重装的值
	TIM_TimeBaseInitStructure.TIM_Period = 100 - 1;	//ARR
	//PSC预分频器的值
	TIM_TimeBaseInitStructure.TIM_Prescaler = 720 - 1;	//PSC
	//重复计数器的值  指定重复计数器值。 每次RCR向下计数器  达到零，生成一个更新事件并重新开始计数  从RCR值 
	TIM_TimeBaseInitStructure.TIM_RepetitionCounter = 0;
	
	//初始化TIMx时间基准单元外设  * TIM_TimeBaseInitStruct中指定的参数
	TIM_TimeBaseInit(TIM3,&TIM_TimeBaseInitStructure);
	
	

	TIM_OCStructInit(&TIM_OCInitStructure);
	//输出的模式
	TIM_OCInitStructure.TIM_OCMode = TIM_OCMode_PWM1;
	//设置输出比较的极性	高电平
	TIM_OCInitStructure.TIM_OCPolarity = TIM_OCPolarity_High;
	//设置输出使能
	TIM_OCInitStructure.TIM_OutputState = TIM_OutputState_Enable;
	//设置CCR
	TIM_OCInitStructure.TIM_Pulse = 0;
	
	//GPIO口PWM控制通道
	TIM_OC1Init(TIM3,&TIM_OCInitStructure);
	TIM_OC2Init(TIM3,&TIM_OCInitStructure);
	TIM_OC3Init(TIM3,&TIM_OCInitStructure);
	
	//启用指定TIM外设
	TIM_Cmd(TIM3,ENABLE);	
}

void PWM_SetRGB_Led(uint16_t red,uint16_t blue)
{
	//使用通道1PWM
	TIM_SetCompare1(TIM3,red);
	//使用通道2PWM
	TIM_SetCompare2(TIM3,blue);

}