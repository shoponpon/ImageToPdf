#ImageToPdf
======================
フォルダ分けされた画像をフォルダごとにPDF出力するソフトウェア。  
 
使い方
------
1.imgfile以下にフォルダを配置、さらにこのフォルダの中に画像を配置する
例：  imgfile/Sample/320x240.png
      imgfile/Sample/640x480.png
      imgfile/Sample/800x600.png
2.ImageToPdf.jarをダブルクリック、あるいはコマンドライン、ターミナルから下記のコマンドを実行
  java -jar ImageToPdf.jar
3.「読込中…」→「変換中…」→「完了！」の順でタスクトレイからメッセージを表示、「完了！」の５秒後にソフトが終了する
4.pdffile内に「フォルダ名.pdf」として出力
例：  pdffile/Sample.pdf
 
関連
--------
uchicomLabs - ウチコムラボ
http://labs.uchicom.com/pdfbox/create_pagesize.jsp

ライセンス
----------
Copyright 2015 shoponpon(SHO OISHI) <shoponprog@gmail.com>
Licensed under the [Apache License, Version 2.0][Apache]
[Apache]: http://www.apache.org/licenses/LICENSE-2.0
