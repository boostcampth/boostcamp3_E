package com.teame.boostcamp.myapplication.ui.signup;

import com.teame.boostcamp.myapplication.model.entitiy.User;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SignUpContract {

    interface View extends BaseView {

        void succeedEmailValidation();

        void occurEmailDuplication();

        void occurEmailFormatError();

        void succeedSignUp();

        void occurSignUpError();

        void showSignUpLoading(boolean visibility);

    }

    interface Presenter extends BasePresenter {
        void checkEmailValidation(String email);

        void doSignUp(String email, String password, User userData);
    }
}
