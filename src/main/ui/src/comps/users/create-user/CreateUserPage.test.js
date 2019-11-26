import { createStore, applyMiddleware } from "redux";
import "@testing-library/jest-dom/extend-expect";
import React from "react";
import { render, wait, fireEvent } from "@testing-library/react";
import { Provider } from "react-redux";
import thunk from "redux-thunk";
import mainReducer from "../../../reducers/main-reducer";
import * as axiosService from "../../../services/axios-service";
import UsersPageContainer from "../UsersPageContainer";

describe('redux integration tests', () => {

    let store;
    let mockLogOn;

    const initialState = {
        user: {userName: 'admin', token: 'nasfdjkbkjsdakjaqw34dscf', page: '/users/create', ip: '192.168.1.25'},
        isLoading: false,
        isError: false,
        message: "",
        isShowMsg: false,
        errorMsg: "",
        isLoggedOn: true,
        isLoggingOn: false,
        isUserLoading: false,
        isUserError: false,
        userMsg: ''
    };

    beforeEach(() => {
        store = createStore(mainReducer, initialState, applyMiddleware(thunk));
    });
    afterEach(() => {});

    test('create user page renders', async () => {

        const { container, getByText } = render(
            <Provider store={store}>
                <UsersPageContainer/>
            </Provider>
        );

        await wait();

        expect(container.querySelector("div.usersPageContainer")).toBeInTheDocument();

        expect(container.querySelector("div.createUserPageContainer")).toBeInTheDocument();

        expect(container.querySelector("header.createUserPage")).toBeInTheDocument();

        expect(container.querySelector("section.createUserPage")).toBeInTheDocument();

        expect(container.querySelector("nav.createUserPage")).toBeInTheDocument();

        expect(container.querySelector("aside.createUserPage")).toBeInTheDocument();

        expect(container.querySelectorAll("tr").length).toBe(5);

        expect(getByText(/Username/)).toBeInTheDocument();

        expect(getByText(/First Name/)).toBeInTheDocument();

        expect(getByText(/Last Name/)).toBeInTheDocument();

        expect(getByText(/Password/)).toBeInTheDocument();

        expect(container.querySelectorAll("input").length).toBe(4);

        expect(container.querySelectorAll("button").length).toBe(4);
    })

})