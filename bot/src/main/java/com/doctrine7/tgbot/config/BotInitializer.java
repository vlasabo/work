package com.doctrine7.tgbot.config;

import com.doctrine7.model.bot.MyBotCommand;
import com.doctrine7.tgbot.service.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
            bot.updateCommands(getCommandsList());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private List<BotCommand> getCommandsList() {
        List<BotCommand> listOfCommands = new ArrayList<>();

        Arrays.stream(MyBotCommand.values())
                .forEach(myBotCommand -> listOfCommands.add(
                                new BotCommand(myBotCommand.getCommand(), myBotCommand.getDescription())
                        )
                );

        return listOfCommands;
    }

}
