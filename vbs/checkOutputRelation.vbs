Set objExcel = CreateObject("Excel.Application")
 objExcel.Visible = false 
Set objWorkbook = objExcel.Workbooks.Open ("C:\Users\Administrator\Desktop\MRDEMO\test\knn\KNN_1_20160805172000.xlsm")
 objExcel.Run " checkOutputRelation"
 objWorkbook.Save
objWorkbook.Close 
objExcel.Quit 
Set objWorkbook = nothing
Set objExcel= nothing 