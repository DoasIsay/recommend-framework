# recommend-framework
推荐框架

    一个使用Java实现的推荐框架
    框架基于配置描述，编排调度推荐流程中各阶段算子执行（并行的用户特征获取/召回算子，串行的过滤/排序/调权/重排算子及异步的日志落盘算子）
    提供基本的推荐日志/词典/配置/算子的管理功能，支持算子热更新，特征/召回/过滤/排序/调权/重排等推荐流程通过配置中心可实时热插拔                             
    使用规则引擎结合实验参数开关来判断推荐断策略算子是否开启生效
    各阶段抽象基类算子提供算子的基本能力及通用逻辑封装，只需继承各阶段抽象基类算子，实现抽象业务接口开发业务逻辑然后在配置中描述即可，而无需关注复杂的推荐流程及数据流转
    
    
配置

    {
      "functors": [
        {"manager-comment": "manager配置"},
    
        {
          "type": "manager",
          "name": "MainManager",
          "functor": "Manager",
          "comment": "顶级管理算子，串行调度执行子管理算子",
          "functors": ["BeginManager", "FeatureManager", "TagManager", "RecallManager", "FilterManager", "SortManager", "AdjustManager", "ResortManager", "PositionManager", "EndManager"],
          "config": {
            "open": true
          }
        },
        {
          "type": "manager",
          "name": "BeginManager",
          "functor": "Manager",
          "comment": "串行执行一些准备工作，算子间有依赖，后边算子会依赖前边算子的输出",
          "functors": ["GetToken", "RequestToUserContext", "ABExpParam"],
          "config": {
            "open": true
          }
        },
        {
          "type": "manager",
          "name": "FeatureManager",
          "functor": "Manager",
          "comment": "并行获取用户特征，算子间无依赖",
          "functors": ["SexFeature", "SessionFeature", "SimpleFeature"],
          "config": {
            "open": true,
            "mode": "parallel"
          }
        },
        {
          "type": "manager",
          "name": "TagManager",
          "functor": "Manager",
          "comment": "并行打标签，算子间无依赖",
          "functors": ["SimpleTag"],
          "config": {
            "open": true,
            "mode": "parallel"
          }
        },
        {
          "type": "manager",
          "name": "RecallManager",
          "functor": "RecallManager",
          "comment": "并行召回",
          "functors": ["SimpleRedis", "SessionRecall", "PosRecall"],
          "config": {
            "open": true,
            "mode": "parallel"
          }
        },
        {
          "type": "manager",
          "name": "FilterManager",
          "functor": "Manager",
          "comment": "串行执行过滤",
          "functors": ["BizFilter"],
          "config": {
            "open": true
          }
        },
        {
          "type": "manager",
          "name": "SortManager",
          "functor": "Manager",
          "comment": "排序，依次顺序执行算子，任何一个算子执行成功则停止执行后边的算子",
          "functors": ["PredictSort", "SimpleSort"],
          "config": {
            "open": true,
            "mode": "cutoff"
          }
        },
        {
          "type": "manager",
          "name": "AdjustManager",
          "functor": "Manager",
          "comment": "调权",
          "functors": ["ChnAdjust1", "ChnAdjust2"],
          "config": {
            "open": true
          }
        },
        {
          "type": "manager",
          "name": "ResortManager",
          "functor": "Manager",
          "comment": "重排",
          "functors": ["SimpleResort"],
          "config": {
            "open": true,
            "mode": "cutoff"
          }
        },
        {
          "type": "manager",
          "name": "PositionManager",
          "functor": "Manager",
          "comment": "串行执行，前面算子优先占位，后边算子继续占剩余的位",
          "functors": ["StrongPosition", "ChnPosition", "SimplePosition"],
          "config": {
            "open": true
          }
        },
        {
          "type": "manager",
          "name": "EndManager",
          "functor": "Manager",
          "comment": "返回请求结果，日志落盘",
          "functors": ["EventToResponse", "LogDump"],
          "config": {
            "open": true,
            "mode": "parallel"
          }
        },
    
        {"functor-comment": "functor 配置"},
        {
          "type": "begin",
          "name": "GetToken",
          "functor": "GetToken",
          "config": {
            "open": true
          }
        },
        {
          "type": "begin",
          "name": "RequestToUserContext",
          "functor": "RequestToUserContext",
          "config": {
            "open": true
          }
        },
        {
          "type": "begin",
          "name": "ABExpParam",
          "functor": "ABExpParam",
          "config": {
            "open": true
          }
        },
        {
          "type": "feature",
          "name": "SimpleFeature",
          "functor": "SimpleFeature",
          "config": {
            "open": true
          }
        },
        {
          "type": "feature",
          "name": "SexFeature",
          "functor": "SexFeature",
          "config": {
            "open": true
          }
        },
        {
          "type": "tag",
          "name": "SimpleTag",
          "functor": "ExpressTag",
          "config": {
            "open": true,
            "tagExpress": "if(context.age==0) {tag.NewUser=true;} if(context.age!=0){tag.OldUser=true;}"
          }
        },
        {
          "type": "recall",
          "name": "SessionRecall",
          "functor": "SessionRecall",
          "exceptNum": 100,
          "config": {
            "open": true,
            "comment": "规则表达示圏用户",
            "express": "tag.NewUser!=nil",
            "comment": "种子过滤规则",
            "seedExpress": "dr>10 && views>1 || likes>0 || pay>1"
          }
        },
        {
          "type": "recall",
          "name": "SimpleRedis",
          "functor": "SimpleRedis",
          "config": {
            "open": true
          }
        },
        {
          "type": "recall",
          "name": "PosRecall",
          "functor": "PosRecall",
          "config": {
            "open": true
          }
        },
        {
          "type": "filter",
          "name": "SimpleFilter",
          "functor": "SimpleFilter",
          "config": {
            "open": true
          }
        },
        {
          "type": "filter",
          "name": "BizFilter",
          "functor": "BizFilter",
          "config": {
            "open": true
          }
        },
        {
          "type": "filter",
          "name": "CoverFilter",
          "functor": "SimpleFilter",
          "config": {
            "open": true,
            "comment": "规则表达示圏用户",
            "express": "tag.OldUser!=nil && 'ios'==context.os && context.offset<100"
          }
        },
        {
          "type": "sort",
          "name": "SimpleSort",
          "functor": "SimpleSort",
          "config": {
            "open": true,
            "comment": "规则表达示圏用户",
            "express": ""
          }
        },
        {
          "type": "adjust",
          "name": "ChnAdjust1",
          "functor": "ChnAdjust",
          "config": {
            "open": true,
            "chn": "SimpleRedis",
            "comment": "规则表达示圏用户",
            "express": "'ios'==context.os && context.offset==0"
          }
        },
        {
          "type": "adjust",
          "name": "ChnAdjust2",
          "functor": "ChnAdjust",
          "config": {
            "open": true,
            "chn": "SimpleRedis",
            "comment": "规则表达示圏用户",
            "express": "tag.OldUser!=nil"
          }
        },
        {
          "type": "position",
          "name": "ChnPosition",
          "functor": "ChnPosition",
          "config": {
            "open": true,
            "chnPos": "SessionRecall:4,5,6"
          }
        },
        {
          "type": "position",
          "name": "StrongPosition",
          "functor": "StrongPosition",
          "config": {
            "open": true,
            "chn": "PosRecall"
          }
        },
        {
          "type": "position",
          "name": "SimplePosition",
          "functor": "SimplePosition",
          "config": {
            "open": true,
            "comment": "此算子只有Ios新户生效",
            "express": "tag.NewUser!=nil && context.os == 'ios'"
          }
        },
        {
          "type": "end",
          "name": "EventToResponse",
          "functor": "EventToResponse",
          "config": {
            "open": true
          }
        },
        {
          "type": "end",
          "name": "LogDump",
          "comment": "日志落盘",
          "functor": "LogDump",
          "config": {
            "open": true
          }
        }
      ]
    }