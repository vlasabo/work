package com.doctrine7.tgbot.service;

import com.doctrine7.tgbot.config.BotConfig;
import com.doctrine7.tgbot.model.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getChatId() > 0) {
            String text = update.getMessage().getText(); //команда
            long chatId = update.getMessage().getChatId();
            sendMessageToId(chatId, "Команда не найдена!");
            logger.error("unrecognized command " + text + " from user @" + update.getMessage().getChat().getUserName());
        }

    }

    public void updateCommands(List<BotCommand> listOfCommands) {
        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), "ru"));
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    public DeliveryStatus sendMessageToId(long chatId, String textToSend) {
        SendMessage outputMessage = new SendMessage();
        outputMessage.setChatId(chatId);
        outputMessage.setText(textToSend);
        outputMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        try {
            execute(outputMessage);
            return DeliveryStatus.GOOD;
        } catch (TelegramApiException e) {
            logger.error(
                    String.format("problems with sending message to chatId %s, text = %s", chatId, textToSend) + e.getMessage());
            if (e.getMessage().contains("bot was blocked by the user")) {
                return DeliveryStatus.BLOCKED;
            } else {
                return DeliveryStatus.BAD;
            }
        }
    }

    private ReplyKeyboardMarkup initKeyboard(LocalDate localDate) {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(true); //скрываем после использования

        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        //Добавляем кнопки с текстом в наш ряд
        LocalDate endDay = localDate.withDayOfMonth(localDate.lengthOfMonth());
        int i = 0;
        //подгоняем расположение кнопок под календарные дни недели начала месяца
        while (localDate.getDayOfWeek().getValue() > keyboardRow.size() + 1) {
            keyboardRow.add(" ");
            i++;
        }

        while (localDate.isBefore(endDay) || localDate.isEqual(endDay)) {
            keyboardRow.add(new KeyboardButton(localDate.format(DateTimeFormatter.ofPattern("dd.MM"))));
            localDate = localDate.plusDays(1);
            i++;
            if (i >= 7) {
                keyboardRow = new KeyboardRow();
                keyboardRows.add(keyboardRow);
                i = 0;
            }
        }
        while (keyboardRow.size() < 7) { //а тут подгонка в конце
            keyboardRow.add(" ");
            i++;
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
