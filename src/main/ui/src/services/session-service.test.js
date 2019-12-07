
import {writeToken, readToken, isSessionStorageAvailible} from "./session-service";

describe('session storage tests', () => {
    test('write to storage returns false', () => {
        expect(isSessionStorageAvailible()).toBe(true);
        expect(writeToken('some_token')).toBe(true);
    });

    test('read from storage returns undefined', () => {
        expect(isSessionStorageAvailible()).toBe(true);
        expect(readToken()).toBe('some_token');
    });

})