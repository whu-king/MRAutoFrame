Set objExcel = CreateObject("Excel.Application")
 objExcel.Visible = false 
Set objWorkbook = objExcel.Workbooks.Open ("C:\Users\Administrator\Desktop\MRDEMO\test\sin\replicate.xlsm")
 objExcel.Run " generateFollowInput"
 objWorkbook.Save
objWorkbook.Close 
objExcel.Quit 
Set objWorkbook = nothing
Set objExcel= nothing 
