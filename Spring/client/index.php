<html>
	<head>
		<meta charset="utf-8" />
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
		<script>
			$(document).ready(function(){
				var document = {
					name_doc:"Документ1",
					status_doc:0,
					createdate_doc:0,
					updatedate_doc:0,
					description_doc:"Описание документа"
				}
				$.post("http://localhost:8080/setdoc",{document:JSON.stringify(document)},
				function(data){
					console.log(1);
				});
			});
		</script>
	</head>
	<body>
	</body>
</html>
