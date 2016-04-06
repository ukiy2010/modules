package com.ukiy.updator;

/**
 * Created by UKIY on 2016/4/6.
 */
public enum StateMachine {
    IDLE {
        @Override
        StateMachine doSomething(Action action) {
            switch (action) {
                case DOWNLOAD:

                    break;
                case CHECK:
                    break;
                default:
                    break;
            }
            return null;
        }
    }, CHECKING {
        @Override
        StateMachine doSomething(Action action) {
            switch (action) {
                case DOWNLOAD:

                    break;
                case CHECK:
                    break;
                default:
                    break;
            }
            return null;
        }
    }, DOWNLOADING {
        @Override
        StateMachine doSomething(Action action) {
            switch (action) {
                case DOWNLOAD:
                    //todo cancel download
                    //todo download
                    break;
                case CHECK:
                    break;
                default:
                    break;
            }
            return null;
        }
    };

    static StateMachine state = IDLE;

    abstract StateMachine doSomething(Action action);

    enum Action {
        DOWNLOAD, CHECK
    }
}
