<?php
 $host        = "host=ec2-54-83-3-38.compute-1.amazonaws.com";
      $port        = "port=5432";
      $dbname      = "dbname=dfaje2dc0u5r2t";
      $credentials = "user=lodiyhiqoqvhkj password=OYxjiZcGZEMf8jYLnUe9vcOOAs";

      $db = pg_connect( " $url $host $port $dbname $credentials"  );
      if(!$db){
       echo "Error : Unable to open database\n";
      } else {
       echo "Opened database successfully\n";
      }
?>
<html>
 <head>
  <title>Employees</title>
 </head>
 <body>
  <table>
   <thead>
    <tr>
     <th>Last Name</th>
     <th>First Name</th>
     <th>Title</th>
    </tr>
   </thead>
   <tbody>
<?php
$query = "SELECT last_name, first_name, title "
     . "FROM employees ORDER BY last_name ASC, first_name ASC";
//$result = $db->query($query);
$result = pg_query($query);
while ($row = pg_fetch_row($result)) {
    echo "<tr>";
    echo "<td>" . htmlspecialchars($row["last_name"]) . "</td>";
    echo "<td>" . htmlspecialchars($row["first_name"]) . "</td>";
    echo "<td>" . htmlspecialchars($row["title"]) . "</td>";
    echo "</tr>";
}
$result->closeCursor();
?>
   </tbody>
  </table>
 </body>
</html>