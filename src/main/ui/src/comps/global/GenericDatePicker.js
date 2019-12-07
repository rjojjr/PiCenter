import React, {useState} from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

/**
 * Takes a currentDate and changeDate function from state.
 * @param currentDate: Date
 * @param changeDate: function(Date)
 * @returns {*}
 * @constructor
 */
const GenericDatePicker = ({currentDate, changeDate}) => {

    //onst [date, setDate] = useState(new Date(Date.now()));

    const handleChange = (date) =>{
        changeDate(date);
    };

    return (
        <DatePicker
            selected={currentDate}
            onChange={handleChange}
        />
    );

};
export default GenericDatePicker;