document
  .addEventListener(
    "DOMContentLoaded",
    function(e) {
      const datepicker = document
        .getElementById('js-datepicker');
      const datepickerValue = document
        .getElementById('datepickerValue');
      const result = document
        .getElementById('result');
      const weekAry = ['（日）', '（月）',
        '（火）', '（水）', '（木）',
        '（金）', '（土）'];
      let datepickerAry = new Array;
      let datepickerTextAry = new Array;
      var dateFirst = new Date();
      var dateEnd = new Date();
      dateFirst.setDate(1);
      dateEnd.setMonth(dateEnd
        .getMonth() + 2);
      dateEnd.setDate(0);
      var date2 = dateFirst
        .setMonth(dateFirst
          .getMonth() + 1);
      //jQieryオブジェクト
      const $datepicker = $(datepicker);
      $datepicker
        .multiDatesPicker({
          maxPicks: 8,
          monthNames: [
            '1月', '2月',
            '3月', '4月',
            '5月', '6月',
            '7月', '8月',
            '9月',
            '10月',
            '11月',
            '12月'],
          monthNamesShort: [
            '1月', '2月',
            '3月', '4月',
            '5月', '6月',
            '7月', '8月',
            '9月',
            '10月',
            '11月',
            '12月'],
          dayNames: ['日曜日',
            '月曜日',
            '火曜日',
            '水曜日',
            '木曜日',
            '金曜日',
            '土曜日'],
          dayNamesMin: [
            '日', '月',
            '火', '水',
            '木', '金',
            '土'],
          dateFormat: 'yy/mm/dd',
          nextText: '次へ',
          prevText: '前へ',
          showOtherMonths: true,
          selectOtherMonths: false,
          firstDay: 1,
          isRTL: false,
          showMonthAfterYear: true,
          yearSuffix: '年',
          /*   numberOfMonths: ($(window).width() > 767) ? 2 : 1,*/
          numberOfMonths: 1,
          showCurrentAtPos: 0,
          minDate: new Date(
            date2),
          maxDate: new Date(
            dateEnd),
          onSelect: function(
            dateText,
            inst) {
            const index = datepickerAry
              .indexOf(dateText);
            const day = new Date(
              dateText);
            const stringDay = dateText;
            if (datepickerAry
              .indexOf(dateText) < 0) {
              datepickerAry
                .push(dateText);
              datepickerTextAry
                .push(stringDay);
              datepickerValue.value = datepickerAry;
            } else {
              datepickerAry
                .splice(
                  index,
                  1);
              datepickerTextAry
                .splice(
                  index,
                  1);
              datepickerValue.value = datepickerAry;
            }
            const datepickerTextStr = datepickerTextAry
              .join(',');
            result.textContent = datepickerTextStr
              .replace(
                /,/g,
                '、');
          }
        });

      $datepicker.datepicker(
        "setDate", new Date(
          date2));

    });