$(function() {

  // テーブルからデータを取得
  var table = $('table#csv-target tr').map(function(i) {
    return $(this).find('th,td').map(function() {
      return $(this).text()
    });
  });

  // CSVデータ整形
  var csv = table.map(function(i, row){return row.toArray().join(',');}).toArray().join('\r\n');

  // Excelの文字化け対策
  var bom = new Uint8Array([0xEF, 0xBB, 0xBF])

  // ダウンロードボタン設置
  var blob = new Blob([bom, csv], { type: 'text/csv' })
  var url = (window.URL || window.webkitURL).createObjectURL(blob)
  var a = document.getElementById('download')
  a.download = 'サンプル.csv'
  a.href = url

});