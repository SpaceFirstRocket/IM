package com.yrw.im.common.parse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.yrw.im.common.exception.ImException;
import com.yrw.im.proto.constant.MsgTypeEnum;
import com.yrw.im.proto.generate.Chat;
import com.yrw.im.proto.generate.Internal;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2019-04-14
 * Time: 16:09
 *
 * @author yrw
 */
public class ParseService {

    private Map<MsgTypeEnum, Parse> parseFunctionMap;

    @FunctionalInterface
    public interface Parse {
        Message process(byte[] bytes) throws InvalidProtocolBufferException;
    }

    public ParseService() {
        parseFunctionMap = new HashMap<>(MsgTypeEnum.values().length);

        parseFunctionMap.put(MsgTypeEnum.CHAT, Chat.ChatMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.INTERNAL, Internal.InternalMsg::parseFrom);
    }

    public Message getMsg(MsgTypeEnum msgType, byte[] bytes) throws InvalidProtocolBufferException {
        Parse parseFunction = parseFunctionMap.get(msgType);
        if (parseFunction == null) {
            throw new ImException("[IM proto msg parse], no proper parse function, msgType: {}", msgType.name());
        }
        return parseFunction.process(bytes);
    }
}
