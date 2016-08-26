(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .controller('InscrireDeleteController',InscrireDeleteController);

    InscrireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inscrire'];

    function InscrireDeleteController($uibModalInstance, entity, Inscrire) {
        var vm = this;

        vm.inscrire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Inscrire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
