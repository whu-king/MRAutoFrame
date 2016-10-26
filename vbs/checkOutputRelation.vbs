Set objExcel = CreateObject("Excel.Application")
 objExcel.Visible = false 
Set objWorkbook = objExcel.Workbooks.Open ("C:\Users\Administrator\Desktop\MRDEMO\test\sin\sin_2016_10_26_14_40_44.xlsm")
 objExcel.Run " checkOutputRelation"
 objWorkbook.Save
objWorkbook.Close 
objExcel.Quit 
Set objWorkbook = nothing
Set objExcel= nothing 
