Set objExcel = CreateObject("Excel.Application")
 objExcel.Visible = false 
Set objWorkbook = objExcel.Workbooks.Open ("F:\619-3.xlsm")
 objExcel.Run " generateFollowOutput"
 objWorkbook.Save
objWorkbook.Close 
objExcel.Quit 
Set objWorkbook = nothing
Set objExcel= nothing 
