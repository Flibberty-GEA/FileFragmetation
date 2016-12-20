package com.sysgears.example;

import com.sysgears.example.controller.StreamController;

import java.io.IOException;

/**
 * @author Yevgen Goliuk
 */
public class Executor {
    StreamController controller;
    ApacheCliParser parser;


    public Executor(final StreamController controller, final ApacheCliParser parser) {
        this.controller = controller;
        this.parser = parser;
    }


    public void execute(){
        controller.sendMessage("Hello!");


        while (true) {
            controller.sendMessage("\nEnter parameters OR 'exit' to quit program\n");
            String inputRequest = controller.getRequest();
            Command command = Command.getCommandByValue(inputRequest);



            try {
                command.apply(controller, inputRequest);

            } catch (IOException e) {
                controller.sendMessage("RequestController cath Exception "+e.getClass());
            }
        }

    }
}
