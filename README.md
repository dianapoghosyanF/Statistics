 ![Alt text](RHV.png?raw=true )
 # Statistics
 
 ## Introduction
 
	Statistics application have a restful two APIs for statistics. 
	One of APIs is called every time a transaction is made. It is also the sole input of this rest API. 
	The other one returns the statistic based on the transactions of the last 60 seconds. 
	And executes in constant O(k) time and space using constant k = 60 length array, 
	where each element of array contains data for one second:
	(array[0] contains all data posted 1 seconds ago befor current UTC time, array[59] - 60 seconds ago)
	
	The application has some bug, and dont have time to fixe at this point.
  
#### REST api 

* **URL:**  `/transactions`
    * *Method:*  `POST`
	* *Body:* 
		   ``
			{
			"amount": 12.3,
			"timestamp": 1478192204000
			}
		   ``
    * *Success Response:* *Code:* `201` <br />
    * *Error Response:*
        * *Code:* `204 - if transaction is older than 60 seconds` <br />
        or
        * *Code:* `500 Internal Server Error` <br />
* **URL:**  `/statistics`
    * *Method:*  `GET`
        * *Success Response:* *Code:* `200` <br />
            *Content:* 
               ``
				{
				"sum": 1000,
				"avg": 100,
				"max": 200,
				"min": 50,
				"count": 10
				}
               ``
        * *Error Response:* *Code:* `500 Internal Server Error` <br />
            *Content:*
                 ``
				 errorText
                ``


