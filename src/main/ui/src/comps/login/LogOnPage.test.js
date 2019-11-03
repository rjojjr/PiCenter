import {createStore, applyMiddleware} from 'redux';
import '@testing-library/jest-dom/extend-expect';
import React from 'react';
import {render, wait, fireEvent} from '@testing-library/react';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import mainReducer from '../../reducers/main-reducer';
import * as axiosService from '../../services/axios-service';
import LogOnPage from "./LogOnPage";
import LogOnPageContainer from "./LogOnPageContainer";

describe('mock axios-service logon: LogOnPageContainer render tests', () => {
    let store;
    let mockLogOn;
    beforeEach(() => {
        store = createStore(mainReducer, applyMiddleware(thunk));
        mockLogOn = jest.fn();
        axiosService.logOn = mockLogOn;
    });

    afterEach(() => {
        mockLogOn.mockClear();
    });

    test('renders logon page', () => {

        const {container} = render(
            <Provider store={store}>
                <LogOnPageContainer/>
            </Provider>
        )

        expect(
            container.querySelector('div.logOnForm')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);

    })

    test('mock successful logon returns valid user', () => {
        
        mockLogOn.mockReturnValue(Promise.resolve({ data:{ user: {}} }))

        const {container} = render(
            <Provider store={store}>
                <LogOnPageContainer/>
            </Provider>
        )

        expect(
            container.querySelector('div.logOnForm')
        ).toBeInTheDocument();

        expect(
            container.querySelector('input.logonInput')
        ).toBeInTheDocument();

        expect(
            container.querySelector('button')
        ).toBeInTheDocument();

        expect(
            container.querySelectorAll('input.logonInput').length
        ).toBe(2);

        const userNameInput = container.querySelectorAll('input')[0];
        const passwordInput = container.querySelectorAll('input')[1];

        fireEvent.change(userNameInput, {target: {value: 'abcd'}});
        fireEvent.change(passwordInput, {target: {value: 'abcd'}});

        const logOnButton = container.querySelectorAll('button')[0];

        fireEvent.click(logOnButton);
    })

});

describe('LogOnPage event tests', () => {
    test('logon page events work', () => {

        const mockResetIsShowMsg = jest.fn();
        const mockLogOn = jest.fn();

        const {container} = render(
            <LogOnPage user={{userName: undefined}} logOn={mockLogOn} resetIsShowMsg={mockResetIsShowMsg} isShowMsg={false} message={''} isLoggingOn={false}/>
        );

        const userNameInput = container.querySelectorAll('input')[0];
        const passwordInput = container.querySelectorAll('input')[1];

        fireEvent.change(userNameInput, {target: {value: 'abcd'}});

        expect(
            mockResetIsShowMsg
        ).toHaveBeenCalledTimes(1);

        fireEvent.change(passwordInput, {target: {value: 'abcd'}});

        expect(
            mockResetIsShowMsg
        ).toHaveBeenCalledTimes(2);

        const logOnButton = container.querySelectorAll('button')[0];

        fireEvent.click(logOnButton);

        expect(
            mockLogOn
        ).toHaveBeenCalledWith('abcd', 'abcd');

    });
});