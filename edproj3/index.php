<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require 'vendor/autoload.php';
function connect_to_db()
{
	$dbconnection = new PDO('mysql:host=localhost;dbname=project3','testuser','test123');
	return $dbconnection;
}

$app = new \Slim\App;
$app->get('/api/getallusers', function(Request $request, Response $response)
{
	$sql_query="SELECT id, username FROM user_info";
	try
	{
		$datab = connect_to_db();
		$stmt = $datab->query($sql_query);
		$userinfo = $stmt->fetchAll(PDO::FETCH_OBJ);
		$datab=null;
		echo json_encode($userinfo);
	}
	catch(PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});
$app->get('/api/getmessages', function(Request $request, Response $response)
{
    $srcuserid = $request->getParam('srcuserid');
	$destuserid = $request->getParam('destuserid');
	$sql_query="SELECT messages.id, messages.src_user_id, user_info.username AS src_user_username, messages.dest_user_id, messages.message, messages.datetime
	FROM messages LEFT JOIN user_info ON messages.src_user_id = user_info.id
	WHERE (src_user_id = $srcuserid AND dest_user_id = $destuserid) OR (src_user_id = $destuserid AND dest_user_id = $srcuserid)
	ORDER BY messages.datetime";
	try
	{
		$datab = connect_to_db();
		$stmt = $datab->query($sql_query);
		$messages = $stmt->fetchAll(PDO::FETCH_OBJ);
		$datab=null;
		echo json_encode($messages);
	}
	catch(PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});
$app->get('/api/getcontactedusers', function(Request $request, Response $response)
{
    $srcuserid = $request->getParam('srcuserid');
	$sql_query="SELECT DISTINCT src_user_id, dest_user_id FROM messages WHERE src_user_id = $srcuserid OR dest_user_id = $srcuserid";
	try
	{
		$datab = connect_to_db();
		$stmt = $datab->query($sql_query);
		$destusers = $stmt->fetchAll(PDO::FETCH_OBJ);
		$datab=null; 
		echo json_encode($destusers);
	}
	catch(PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});
$app->get('/api/getuser', function(Request $request, Response $response)
{
	$username = $request->getParam('username');
	$password = $request->getParam('password');
	$sql_query="SELECT id FROM user_info WHERE username = '$username' AND password = '$password'";
	try
	{
		$datab = connect_to_db();
		$stmt = $datab->query($sql_query);
		$userinfo = $stmt->fetchAll(PDO::FETCH_OBJ);
		$datab=null;
		echo json_encode($userinfo);
	}
	catch(PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});

$app->post('/api/adduser', function (Request $request, Response $response)
{
	$username = $request->getParam('username');
	$password = $request->getParam('password');
	$sql_query = "INSERT INTO user_info (id, username, password) VALUES (NULL, :username, :password)";
	try
	{
		$datab=connect_to_db();
		$stmt=$datab->prepare($sql_query);
		$stmt->bindParam(':username',$username);
		$stmt->bindParam(':password',$password);
		$result = $stmt->execute();
		$datab=null;
		echo json_encode($result);
	}
	catch (PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});
	
$app->post('/api/addmessage', function (Request $request, Response $response)
{
	$srcuserid = $request->getParam('srcuserid');
	$destuserid = $request->getParam('destuserid');
	$msg = $request->getParam('msg');
	$datetime = $request->getParam('datetime');
	$sql_query = "INSERT INTO messages (id, src_user_id, dest_user_id, message, datetime) VALUES (NULL, :srcuserid, :destuserid, :msg, :datetime)";
	try
	{
		$datab=connect_to_db();
		$stmt=$datab->prepare($sql_query);
		$stmt->bindParam(':srcuserid',$srcuserid);
		$stmt->bindParam(':destuserid',$destuserid);
		$stmt->bindParam(':msg',$msg);
		$stmt->bindParam(':datetime',$datetime);
		$result = $stmt->execute();
		$datab=null;
		echo json_encode($result);
	}
	catch (PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});

$app->delete('/api/deletemessage', function (Request $request, Response $response)
{
	$msgid = $request->getParam('msgid');
	$sql_query="DELETE FROM messages where id = '$msgid'";
	try
	{
		$datab=connect_to_db();
		$stmt=$datab->prepare($sql_query);
		$stmt->execute();
		$datab=null;
		echo '{"Result:{"text":"Message Deleted"}';
	}
	catch (PDOException $e)
	{
		echo '{"error":{"text":'.$e->getMessage().'}';
	}
});


$app->run();